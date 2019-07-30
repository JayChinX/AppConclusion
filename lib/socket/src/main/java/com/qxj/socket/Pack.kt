package com.qxj.socket

 data class Pack(
     val header: String = Pack.header,
     var content: String?,
     val length: String = getByte(content?.length ?: 0),
     val size: Int = header.toByteArray().size + length.toByteArray().size + (content?.toByteArray()?.size ?: 0)
) {

     init {

     }
    //判头
//    val header = "****"
//    val lengt = getByte(content?.toByteArray()?.size ?: 0)
    // size
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

        const val header = "****"

        const val HEADER = 4

        const val LENGTH = 4

        @JvmStatic
        fun getByte(a: Int): String {
            val bytes = ByteArray(4)
            bytes[0] = (a % 10000 / 1000).toByte()
            bytes[1] = (a % 1000 / 100).toByte()
            bytes[2] = (a % 100 / 10).toByte()
            bytes[3] = (a % 10).toByte()
            return "${bytes[0]}${bytes[1]}${bytes[2]}${bytes[3]}"
        }
    }




    override fun toString(): String {
        return "Pack[" +
                "header=" + header +
                ", length=" + length +
                ", content='" + content + '\'' +
                ']'
    }
}