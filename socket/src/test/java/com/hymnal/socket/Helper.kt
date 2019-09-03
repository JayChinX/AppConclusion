package com.hymnal.socket

import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.core.session.IoSession
import org.slf4j.LoggerFactory

// 类继承handler
internal class ServerHandler : IoHandlerAdapter() {

    @Throws(Exception::class)
    override fun messageReceived(session: IoSession?, message: Any?) {
        val msg = message.toString()
        logger.info("服务端接收" + session!!.id + "消息成功：" + msg)
        val content = amsg
        //向客户端写入数据
        val future = session.write(content.toString())
        //在100毫秒内写完
        future.awaitUninterruptibly(100)
        if (future.isWritten) {

        } else {

        }
    }

    @Throws(Exception::class)
    override fun messageSent(session: IoSession?, message: Any?) {
        val msg = message.toString()
        logger.info("服务端发送" + session!!.id + "消息成功：" + msg)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(session: IoSession?, cause: Throwable) {
        cause.printStackTrace()
        logger.error("服务端" + session!!.id + "处理消息异常：" + cause)
    }

    @Throws(Exception::class)
    override fun sessionIdle(session: IoSession?, status: IdleStatus?) {
        logger.error("服务端" + session!!.id + "进入空闲状态")
    }

    @Throws(Exception::class)
    override fun sessionCreated(session: IoSession?) {
        logger.info("服务端与" + session!!.id + "用户连接")
        super.sessionCreated(session)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ServerHandler::class.java)
    }

    val amsg = "{\n" +
            "    \"msgCnt\":1,\n" +
            "    \"timeStamp\":1565222888,\n" +
            "    \"localInfo\":{\n" +
            "        \"latitude\":30.340164,\n" +
            "        \"longitude\":121.2387135,\n" +
            "        \"elevation\":123.45,\n" +
            "        \"heading\":351.7,\n" +
            "        \"speed\":1.235\n" +
            "    },\n" +
            "    \"remoteInfo\":{\n" +
            "        \"remoteNum\":1,\n" +
            "        \"remoteData\":[\n" +
            "            {\n" +
            "                \"id\":1234567890,\n" +
            "                \"heading\":90.1,\n" +
            "                \"orientation\":1024,\n" +
            "                \"distance\":23.520424589340674,\n" +
            "                \"latitude\":30.3402593,\n" +
            "                \"longitude\":121.238495,\n" +
            "                \"elevation\":123.45,\n" +
            "                \"speed\":1.16,\n" +
            "                \"lights\":2,\n" +
            "                \"events\":64,\n" +
            "                \"alertType\":7\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"spatInfo\":{\n" +
            "        \"spatId\":1234567890,\n" +
            "        \"maneuvers\":16,\n" +
            "        \"lightStatus\":6,\n" +
            "        \"leftTime\":23,\n" +
            "        \"distance\":23.520424589340674,\n" +
            "        \"spatLat\":30.340164,\n" +
            "        \"spatLon\":121.2387135,\n" +
            "        \"speedAdvice\":{\n" +
            "            \"minValue\":20,\n" +
            "            \"maxValue\":40,\n" +
            "            \"type\":2\n" +
            "        },\n" +
            "        \"speedLimit\":{\n" +
            "            \"value\":20,\n" +
            "            \"overSpd\":0\n" +
            "        },\n" +
            "        \"redLightViolate\":1\n" +
            "    },\n" +
            "    \"rsiInfo\":{\n" +
            "        \"rsiId\":1234567890,\n" +
            "        \"rsiType\":46,\n" +
            "        \"rsiValue\":3,\n" +
            "        \"distance\":23.520424589340674,\n" +
            "        \"rsiLat\":30.340164,\n" +
            "        \"rsiLon\":121.2387135,\n" +
            "        \"description\":\"valid if rsiType = 0\",\n" +
            "        \"wpNum\":2,\n" +
            "        \"warningPoint\":[\n" +
            "            {\n" +
            "                \"latitude\":30.340164,\n" +
            "                \"longitude\":121.2387135,\n" +
            "                \"elevation\":123.45\n" +
            "            },\n" +
            "            {\n" +
            "                \"latitude\":30.3401642,\n" +
            "                \"longitude\":121.2387137,\n" +
            "                \"elevation\":123.45\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"ptcInfo\":{\n" +
            "        \"ptcId\":1234567890,\n" +
            "        \"distance\":23.520424589340674,\n" +
            "        \"orientation\":1\n" +
            "    }\n" +
            "}"
}
