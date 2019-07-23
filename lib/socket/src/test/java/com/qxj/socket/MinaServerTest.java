package com.qxj.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;


public class MinaServerTest {
    private static final Logger logger = LoggerFactory.getLogger(MinaServerTest.class);

    // 端口
    private static final int MINA_PORT = 7083;

    public static void main(String[] args) {
        IoAcceptor acceptor;
        try {
            // 创建一个非阻塞的服务端server
            acceptor = new NioSocketAcceptor();
            // 设置编码过滤器（自定义）
            acceptor.getFilterChain().addLast("mycoder",
                    new ProtocolCodecFilter(new CustomProtocolCodecFactory(Charset.forName("UTF-8"))));
            // 设置缓冲区大小
            acceptor.getSessionConfig().setReadBufferSize(1024);
            // 设置读写空闲时间
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            // 绑定handler
            acceptor.setHandler(new ServerHandler());
            // 绑定端口 可同时绑定多个
            acceptor.bind(new InetSocketAddress(MINA_PORT));
            logger.info("创建Mina服务端成功，端口：" + MINA_PORT);
        } catch (IOException e) {
            logger.error("创建Mina服务端出错：" + e.getMessage());
        }
    }
}

// 类继承handler
class ServerHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Packs pack = (Packs) message;
        logger.info("服务端接收" + session.getId() + "消息成功：" + pack);
        String content = "服务端发送测试数据OK";
        Packs pack1 = new Packs((byte) 2, content);
        session.write(pack1);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        Packs pack = (Packs) message;
        logger.info("服务端发送" + session.getId() + "消息成功：" + pack);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.error("服务端" + session.getId() + "处理消息异常：" + cause);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.error("服务端" + session.getId() + "进入空闲状态");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.info("服务端与" + session.getId() + "用户连接");
        super.sessionCreated(session);
    }
}

class Packs {
    /**
     * 0x00表示请求
     */
    public static final byte REQUEST = 0x00;
    /**
     * 0x01表示回复
     */
    public static final byte RESPONSE = 0x01;

    // 总长度（编号字节 + 长度的字节 + 包体长度字节）
    private int len;
    // 版本号
    private byte flag;
    // 发送人，只是服务端-客户端，暂时无需发送人 接收人
    // private long sender;
    // 接收人
    // private long receiver;
    // 包体
    private String content;

    // 构造方法设置协议
    public Packs(byte flag, String content) {
        this.flag = flag;
        this.content = content;
        // 版本类型的长度1个字节， len的长度4个字节， 内容的字节数
        this.len = 1 + 4 + (content == null ? 0 : content.getBytes().length);
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Pack[" +
                "len=" + len +
                ", flag=" + flag +
                ", content='" + content + '\'' +
                ']';
    }

}


class CustomProtocolDecoder extends CumulativeProtocolDecoder {

    private final Charset charset;

    public CustomProtocolDecoder() {
        this.charset = Charset.defaultCharset();
    }

    // 构造方法注入编码格式
    public CustomProtocolDecoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        // 包头的长度
        final int PACK_HEAD_LEN = 5;
        // 拆包时，如果可读数据的长度小于包头的长度，就不进行读取
        if (in.remaining() < PACK_HEAD_LEN) {
            return false;
        }
        if (in.remaining() > 1) {
            // 标记设为当前
            in.mark();
            // 获取总长度
            int length = in.getInt(in.position());
            // 如果可读取数据的长度 小于 总长度 - 包头的长度 ，则结束拆包，等待下一次
            if (in.remaining() < (length - PACK_HEAD_LEN)) {
                in.reset();
                return false;
            } else {
                // 重置，并读取一条完整记录
                in.reset();
                byte[] bytes = new byte[length];
                // 获取长度4个字节、版本1个字节、内容
                in.get(bytes, 0, length);
                byte flag = bytes[4];
                String content = new String(bytes, PACK_HEAD_LEN, length - PACK_HEAD_LEN, charset);
                // 封装为自定义的java对象
                Packs pack = new Packs(flag, content);
                out.write(pack);
                // 如果读取一条记录后，还存在数据（粘包），则再次进行调用
                return in.remaining() > 0;
            }
        }
        return false;
    }
}

class CustomProtocolEncoder implements ProtocolEncoder {

    private final Charset charset;

    public CustomProtocolEncoder() {
        this.charset = Charset.defaultCharset();
    }

    // 构造方法注入编码格式
    public CustomProtocolEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        // 转为自定义协议包
        Packs customPack = (Packs) message;
        // 初始化缓冲区
        IoBuffer buffer = IoBuffer.allocate(customPack.getLen())
                .setAutoExpand(true);
        // 设置长度、报头、内容
        buffer.putInt(customPack.getLen());
        buffer.put(customPack.getFlag());
        if (customPack.getContent() != null) {
            buffer.put(customPack.getContent().getBytes());
        }
        // 重置mask，发送buffer
        buffer.flip();
        out.write(buffer);
    }

    @Override
    public void dispose(IoSession session) throws Exception {

    }
}

class CustomProtocolCodecFactory implements ProtocolCodecFactory {

    private final ProtocolEncoder encoder;
    private final ProtocolDecoder decoder;

    public CustomProtocolCodecFactory() {
        this(Charset.forName("UTF-8"));
    }

    // 构造方法注入编解码器
    public CustomProtocolCodecFactory(Charset charset) {
        this.encoder = new CustomProtocolEncoder(charset);
        this.decoder = new CustomProtocolDecoder(charset);
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}

