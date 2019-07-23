package org.slf4j.impl

import org.slf4j.event.LoggingEvent
import org.slf4j.helpers.MarkerIgnoringBase
import org.slf4j.helpers.MessageFormatter
import java.io.PrintStream
import java.util.*

class SimpleLogger() : MarkerIgnoringBase() {


    private var currentLogLevel = 20
    @Transient
    private var shortLogName: String? = null

    companion object {

        private val serialVersionUID = -632788891211436180L
        private val START_TIME = System.currentTimeMillis()
        protected val LOG_LEVEL_TRACE = 0
        protected val LOG_LEVEL_DEBUG = 10
        protected val LOG_LEVEL_INFO = 20
        protected val LOG_LEVEL_WARN = 30
        protected val LOG_LEVEL_ERROR = 40
        protected val LOG_LEVEL_OFF = 50
        private var INITIALIZED = false
        private lateinit var configParams: SimpleLoggerConfiguration

        internal fun lazyInit() {
            if (!INITIALIZED) {
                INITIALIZED = true
                init()
            }
        }

        private fun init() {
            configParams = SimpleLoggerConfiguration()
            configParams.init()
        }
    }



    constructor(name: String) : this() {
        this.name = name
        val levelString = "info"
        this.currentLogLevel = SimpleLoggerConfiguration.stringToLevel(levelString)

    }

    private fun log(level: Int, message: String, t: Throwable?) {
        if (this.isLevelEnabled(level)) {
            val buf = StringBuilder(32)
            if (configParams.showDateTime) {
                if (configParams.dateFormatter != null) {
                    buf.append(getFormattedDate())
                    buf.append(' ')
                } else {
                    buf.append(System.currentTimeMillis() - START_TIME)
                    buf.append(' ')
                }
            }

            if (configParams.showThreadName) {
                buf.append('[')
                buf.append(Thread.currentThread().name)
                buf.append("] ")
            }

            if (configParams.levelInBrackets) {
                buf.append('[')
            }

            val levelStr = this.renderLevel(level)
            buf.append(levelStr)
            if (configParams.levelInBrackets) {
                buf.append(']')
            }

            buf.append(' ')
            if (configParams.showShortLogName) {
                if (this.shortLogName == null) {
                    this.shortLogName = this.computeShortName()
                }

                buf.append(this.shortLogName.toString()).append(" - ")
            } else if (configParams.showLogName) {
                buf.append(this.name.toString()).append(" - ")
            }

            buf.append(message)
            this.write(buf, t)
        }
    }

    private fun renderLevel(level: Int): String? {
        return when (level) {
            0 -> "TRACE"
            10 -> "DEBUG"
            20 -> "INFO"
            30 -> configParams.warnLevelString
            40 -> "ERROR"
            else -> throw IllegalStateException("Unrecognized level [$level]")
        }
    }

    private fun write(buf: StringBuilder, t: Throwable?) {
        val targetStream = configParams.outputChoice?.getTargetPrintStream()
        targetStream?.println(buf.toString())
        this.writeThrowable(t, targetStream)
        targetStream?.flush()
    }

    private fun writeThrowable(t: Throwable?, targetStream: PrintStream?) {
        t?.printStackTrace(targetStream)

    }

    private fun getFormattedDate(): String {
        val now = Date()
        synchronized(configParams.dateFormatter!!) {
            return configParams.dateFormatter!!.format(now)
        }
    }

    private fun computeShortName(): String {
        return this.name.substring(this.name.lastIndexOf(".") + 1)
    }

    private fun formatAndLog(level: Int, format: String, arg1: Any, arg2: Any?) {
        if (this.isLevelEnabled(level)) {
            val tp = MessageFormatter.format(format, arg1, arg2)
            this.log(level, tp.message, tp.throwable)
        }
    }

    private fun formatAndLog(level: Int, format: String, vararg arguments: Any) {
        if (this.isLevelEnabled(level)) {
            val tp = MessageFormatter.arrayFormat(format, arguments)
            this.log(level, tp.message, tp.throwable)
        }
    }

    private fun isLevelEnabled(logLevel: Int): Boolean {
        return logLevel >= this.currentLogLevel
    }

    override fun isTraceEnabled(): Boolean {
        return this.isLevelEnabled(0)
    }

    override fun trace(msg: String) {
        this.log(0, msg, null)
    }

    override fun trace(format: String, param1: Any) {
        this.formatAndLog(0, format, param1, null)
    }

    override fun trace(format: String, param1: Any, param2: Any) {
        this.formatAndLog(0, format, param1, param2)
    }

    override fun trace(format: String, vararg argArray: Any) {
        this.formatAndLog(0, format, *argArray)
    }

    override fun trace(msg: String, t: Throwable) {
        this.log(0, msg, t)
    }

    override fun isDebugEnabled(): Boolean {
        return this.isLevelEnabled(10)
    }

    override fun debug(msg: String) {
        this.log(10, msg, null as Throwable?)
    }

    override fun debug(format: String, param1: Any) {
        this.formatAndLog(10, format, param1, null as Any?)
    }

    override fun debug(format: String, param1: Any, param2: Any) {
        this.formatAndLog(10, format, param1, param2)
    }

    override fun debug(format: String, vararg argArray: Any) {
        this.formatAndLog(10, format, *argArray)
    }

    override fun debug(msg: String, t: Throwable) {
        this.log(10, msg, t)
    }

    override fun isInfoEnabled(): Boolean {
        return this.isLevelEnabled(20)
    }

    override fun info(msg: String) {
        this.log(20, msg, null as Throwable?)
    }

    override fun info(format: String, arg: Any) {
        this.formatAndLog(20, format, arg, null as Any?)
    }

    override fun info(format: String, arg1: Any, arg2: Any) {
        this.formatAndLog(20, format, arg1, arg2)
    }

    override fun info(format: String, vararg argArray: Any) {
        this.formatAndLog(20, format, *argArray)
    }

    override fun info(msg: String, t: Throwable) {
        this.log(20, msg, t)
    }

    override fun isWarnEnabled(): Boolean {
        return this.isLevelEnabled(30)
    }

    override fun warn(msg: String) {
        this.log(30, msg, null as Throwable?)
    }

    override fun warn(format: String, arg: Any) {
        this.formatAndLog(30, format, arg, null as Any?)
    }

    override fun warn(format: String, arg1: Any, arg2: Any) {
        this.formatAndLog(30, format, arg1, arg2)
    }

    override fun warn(format: String, vararg argArray: Any) {
        this.formatAndLog(30, format, *argArray)
    }

    override fun warn(msg: String, t: Throwable) {
        this.log(30, msg, t)
    }

    override fun isErrorEnabled(): Boolean {
        return this.isLevelEnabled(40)
    }

    override fun error(msg: String) {
        this.log(40, msg, null as Throwable?)
    }

    override fun error(format: String, arg: Any) {
        this.formatAndLog(40, format, arg, null as Any?)
    }

    override fun error(format: String, arg1: Any, arg2: Any) {
        this.formatAndLog(40, format, arg1, arg2)
    }

    override fun error(format: String, vararg argArray: Any) {
        this.formatAndLog(40, format, *argArray)
    }

    override fun error(msg: String, t: Throwable) {
        this.log(40, msg, t)
    }

    private fun log(event: LoggingEvent) {
        val levelInt = event.level.toInt()
        if (this.isLevelEnabled(levelInt)) {
            val tp = MessageFormatter.arrayFormat(event.message, event.argumentArray, event.throwable)
            this.log(levelInt, tp.message, event.throwable)
        }
    }
}