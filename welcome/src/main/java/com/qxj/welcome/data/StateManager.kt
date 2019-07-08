package com.qxj.welcome.data

class StateManager {
    companion object {
        //2进制和16进制的转换关系是：（数字间的空格只为便于阅读）
        //0b0000 0000 0000 0001 = 0x0001
        //0b0000 0000 0000 0010 = 0x0002
        //0b0000 0000 0000 0100 = 0x0004
        //0b0000 0000 0000 1000 = 0x0008
        //0b0000 0000 0001 0000 = 0x0010
        //0b0000 0000 0010 0000 = 0x0020
        //0b0000 0000 0100 0000 = 0x0040
        //0b0000 0000 1000 0000 = 0x0080
        //1、2、4、8，是为了确保在位运算时，每一个二进制位是被独占，以便区分不同状态。

        /**
         * kotlin 位运算
         */
        //  与
        //    a1 and b1
        //  或
        //    a1 or b1
        //  异或
        //    a1 xor b1
        //  按位取反
        //    a1.inv()
        //  左移
        //    a1.shl(1)
        //  右移
        //    a1.shr(1)
        //  无符号右移
        //    a1.ushr(1)
        const val STATUS_1 = 0x0001
        const val STATUS_2 = 0x0002
        const val STATUS_3 = 0x0004
        const val STATUS_4 = 0x0008
        const val STATUS_5 = 0x0010
        const val STATUS_6 = 0x0020
        const val STATUS_7 = 0x0040
        const val STATUS_8 = 0x0080

        private const val MODE_A = STATUS_1 or STATUS_2 or STATUS_3
        private const val MODE_B = STATUS_1 or STATUS_4 or STATUS_5 or STATUS_6
        private const val MODE_C = STATUS_1 or STATUS_7 or STATUS_8

        private var STATUSES = MODE_A

        fun addStatus(status: Int) {
            STATUSES = STATUSES or status
        }

        fun setStatus(status: Int) {
            STATUSES = status
        }

        fun getStatus() = STATUSES

        fun removeStatus(status: Int) {
            STATUSES = STATUSES and (status.inv())
        }

        fun isStatusEnabled(status: Int): Boolean {
            return (STATUSES and status) != 0
        }
    }
}

fun main() {
    StateManager.addStatus(StateManager.STATUS_4)
    println(StateManager.isStatusEnabled(StateManager.STATUS_4))
    StateManager.removeStatus(StateManager.STATUS_4)
    println(StateManager.isStatusEnabled(StateManager.STATUS_4))


}
//有一个位运算口诀大家可以记一下：

//清零取反要用与，某位置一可用或
//若要取反和交换，轻轻松松用异或
//
//判断Int型变量a是奇数还是偶数
//
//a1 and 1 = 0   // 偶数
//a1 and 1 = 0   // 奇数
//
//
//获取Int型变量的第K位(注：K从0开始依次由右往左，以下揭同)
//
//a1 shr k and 1
//
//
//将Int型变量的第K位清0
//
//a1 and ((1 shl k).inv())
//
//
//将Int型变量的第K位置1
//
//a1 or (1 shl k)
//
//
//平均值
//
//(a1 and b1)+((a1 xor b1) shr 1)
//
//
//不用temp交换两个整数
//
//a1 = a1 xor b1
//b1 = b1 xor a1
//a1 = a1 xor b1
//
//
//获取绝对值
//
//val temp = c1 shr 31
//(c1 + temp) xor temp
//(c1 xor temp) - temp
//
//
//获取相反数
//
//c1.inv()+1
//
//
//Int转byte数组
//
//val bytes = ByteArray(4)
//bytes[0] = (a1 and 0xFF).toByte()
//bytes[1] = (a1 shr 8 and 0xFF).toByte()
//bytes[2] = (a1 shr 16 and 0xFF).toByte()
//bytes[3] = (a1 shr 24 and 0xFF).toByte()
//
//作者：皮球二二
//链接：https://www.jianshu.com/p/8d1bc648a4a0
//来源：简书
//简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。