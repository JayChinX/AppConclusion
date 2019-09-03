package com.hymnal.socket.default

data class Pack(
    val header: String = "5aa5",
    val HEADER: Int = 2,
    var content: String? = null,
    val LENGTH: Int = 4
) {
    override fun toString(): String {
        return "Pack[" +
                "header=" + header +
                ", length=" + getLength() +
                ", content='" + content + '\'' +
                ']'
    }

    fun getLength() = content?.toByteArray()?.size ?: 0

    fun getSize() = HEADER + LENGTH + getLength()

    fun getLengthByte() = getByte(getLength())

    fun getHeader() = hexToByteArray(header)

    companion object {
        @JvmStatic
        fun getByte(a: Int): ByteArray {

            return int2ByteArray(a)
        }

        @JvmStatic
        fun getBytesToInt(bytes: ByteArray): Int {
            var num = bytes[0].toInt() and 0xFF
            num = num or (bytes[1].toInt() shl 8 and 0xFF00)
            num = num or (bytes[2].toInt() shl 16 and 0xFF0000)
            num = num or (bytes[3].toInt() shl 24 and 0xFF00000)
            return num
        }



        fun isHex(hex: String): Boolean {
            val regex ="^[A-Fa-f0-9]+$"
            return hex.matches(Regex(regex))
        }

        /**
         * byte数组转int
         *
         * @param bytes 数组
         * @return int
         */
        private fun bytes2Int(bytes: ByteArray): Int {
            var num = bytes[0].toInt() and 0xFF
            num = num or (bytes[1].toInt() shl 8 and 0xFF00)
            num = num or (bytes[2].toInt() shl 16 and 0xFF0000)
            num = num or (bytes[3].toInt() shl 24 and 0xFF00000)
            return num
        }

        /**
         * int转byte数组
         *
         * @param i int
         * @return byte数组
         */
        private fun int2ByteArray(i: Int): ByteArray {
            val result = ByteArray(4)
            result[0] = (i and 0xFF).toByte()
            result[1] = (i shr 8 and 0xFF).toByte()
            result[2] = (i shr 16 and 0xFF).toByte()
            result[3] = (i shr 24 and 0xFF).toByte()
            return result
        }

        /**
         * hex字符串转byte数组
         * @param inHex 待转换的Hex字符串
         * @return  转换后的byte数组结果
         */
        fun hexToByteArray(inHex: String): ByteArray {
            var hex = inHex
            var hexlen = hex.length
            val result: ByteArray
            if (hexlen % 2 == 1) {
                //奇数
                hexlen++
                result = ByteArray(hexlen / 2)
                hex = "0$hex"
            } else {
                //偶数
                result = ByteArray(hexlen / 2)
            }
            var j = 0
            var i = 0
            while (i < hexlen) {
                result[j] = hexToByte(hex.substring(i, i + 2))
                j++
                i += 2
            }
            return result
        }


        /**
         * Hex字符串转byte
         * @param inHex 待转换的Hex字符串
         * @return  转换后的byte
         */
        private fun hexToByte(inHex: String): Byte {
            return Integer.parseInt(inHex, 16).toByte()
        }


        /**
         * 字节数组转16进制
         * @param bytes 需要转换的byte数组
         * @return  转换后的Hex字符串
         */
        fun bytesToHex(bytes: ByteArray): String {
            val sb = StringBuffer()
            for (i in bytes.indices) {
                val hex = Integer.toHexString(bytes[i].toInt() and 0xFF)
                if (hex.length < 2) {
                    sb.append(0)
                }
                sb.append(hex)
            }
            return sb.toString()
        }
    }
}