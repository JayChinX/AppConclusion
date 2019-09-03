package com.hymnal.welcome.data

object Number {

    /**
     * 清零取反要用与(and)，某位置一可用或(or)
     * 若要取反和交换，轻轻松松用异或
     *
     * and   如果对应位都是1，则结果为1，否则为0
     * or    如果对应位都是0，则结果为0，否则为1
     * xor   如果对应位值相同，则结果为0，否则为1
     * inv   按位翻转操作数的每一位，即0变成1，1变成0
     * shl   按位左移指定的位数，相当于乘以2的N次方。移掉的省略，右边缺失的位，用0补齐
     * shr   按位右移指定的位数，相当于除以2的N次方，移掉的省略，左边缺失的位，如果是正数则补0，若为负数，可能补0或补1，这取决于所用的计算机系统
     * ushr  按位右移指定的位数，移掉的省略，左边缺失的位，用0补齐
     */

    //二分查找
    private fun binaryTest(arrs: IntArray, target: Int): Int {
        var start: Int = 0
        var end: Int = arrs.size - 1
        while (start <= end) {
            val middle: Int = (start + end).ushr(1)//无符号右移 表示 java >>> 平均值
            val midVal = arrs[middle]//取中间值
            when {
                midVal == target -> return middle
                midVal < target -> start = middle + 1
                midVal > target -> end = middle - 1
            }
        }
        return start.inv()//0按位取反为-1
    }
}