package com.qxj.socket

data class Pack(
        var flag: Byte,
        var content: String?,
        var len: Int = 1 + 4 + (content?.toByteArray()?.size ?: 0)) {
    // len
    // 总长度（编号字节 + 长度的字节 + 包体长度字节）


    // flag 版本号

    // content
    // 发送人，只是服务端-客户端，暂时无需发送人 接收人
    // private long sender;
    // 接收人
    // private long receiver;
    // 包体
    companion object {
        //请求
        const val REQUEST = 0x00
        //回复
        const val RESPONSE = 0x01
    }

    override fun toString(): String {
        return "Pack[" +
                "len=" + len +
                ", flag=" + flag +
                ", content='" + content + '\'' +
                ']'
    }
}