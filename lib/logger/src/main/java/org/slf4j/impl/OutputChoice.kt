package org.slf4j.impl

import java.io.PrintStream

class OutputChoice {
    private val outputChoiceType: OutputChoiceType
    private var targetPrintStream: PrintStream?

    constructor(outputChoiceType: OutputChoiceType) {
        if (outputChoiceType == OutputChoiceType.FILE) {
            throw IllegalArgumentException()
        } else {
            this.outputChoiceType = outputChoiceType
            when (outputChoiceType) {
                OutputChoiceType.CACHED_SYS_OUT -> this.targetPrintStream = System.out
                OutputChoiceType.CACHED_SYS_ERR -> this.targetPrintStream = System.err
                else -> this.targetPrintStream = null
            }

        }
    }

    constructor(printStream: PrintStream) {
        this.outputChoiceType = OutputChoiceType.FILE
        this.targetPrintStream = printStream
    }

    fun getTargetPrintStream(): PrintStream? {
        return when (this.outputChoiceType) {
            OutputChoiceType.SYS_OUT -> System.out
            OutputChoiceType.SYS_ERR -> System.err
            OutputChoiceType.CACHED_SYS_ERR, OutputChoiceType.CACHED_SYS_OUT, OutputChoiceType.FILE -> this.targetPrintStream
        }
    }

    enum class OutputChoiceType {
        SYS_OUT,
        CACHED_SYS_OUT,
        SYS_ERR,
        CACHED_SYS_ERR,
        FILE
    }
}