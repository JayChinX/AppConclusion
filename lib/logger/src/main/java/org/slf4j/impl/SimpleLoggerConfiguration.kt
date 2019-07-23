package org.slf4j.impl

import org.slf4j.helpers.Util
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.DateFormat
import java.text.SimpleDateFormat

class SimpleLoggerConfiguration {

    //默认级别
    private var defaultLogLevel: Int = 20

    //是否显示时间
    internal var showDateTime: Boolean = false
    //时间格式
    internal var dateFormatter: DateFormat? = null
    //是否显示线程名字
    internal var showThreadName: Boolean = false
    //长名字
    internal var showLogName: Boolean = false
    //短名字
    internal var showShortLogName: Boolean = false
    //是否[]包裹log级别
    internal var levelInBrackets: Boolean = false
    //打印log文件名字
    private var logFile: String = LOG_FILE_DEFAULT
    internal var outputChoice: OutputChoice? = null
    //
    private var cacheOutputStream: Boolean = false

    //报警级别
    internal var warnLevelString: String = "WARN"

    companion object {
        internal var DEFAULT_LOG_LEVEL_DEFAULT = 20
        private val SHOW_DATE_TIME_DEFAULT = false
        private val SHOW_THREAD_NAME_DEFAULT = true
        internal val SHOW_LOG_NAME_DEFAULT = true
        private val SHOW_SHORT_LOG_NAME_DEFAULT = false
        private val LEVEL_IN_BRACKETS_DEFAULT = false
        private val CACHE_OUTPUT_STREAM_DEFAULT = false
        private val WARN_LEVELS_STRING_DEFAULT = "WARN"

        private val DATE_TIME_FORMAT_STR_DEFAULT: String = "yyyy-MM-dd  hh:mm:ss"
        var dateTimeFormatStr: String = DATE_TIME_FORMAT_STR_DEFAULT
        var LOG_FILE_DEFAULT: String = "System.out"

        fun stringToLevel(levelStr: String): Int {
            return if ("trace".equals(levelStr, ignoreCase = true)) {
                0
            } else if ("debug".equals(levelStr, ignoreCase = true)) {
                10
            } else if ("info".equals(levelStr, ignoreCase = true)) {
                20
            } else if ("warn".equals(levelStr, ignoreCase = true)) {
                30
            } else if ("error".equals(levelStr, ignoreCase = true)) {
                40
            } else {
                if ("off".equals(levelStr, ignoreCase = true)) 50 else 20
            }
        }

        private fun computeOutputChoice(logFile: String?, cacheOutputStream: Boolean): OutputChoice {
            return if ("System.err".equals(logFile!!, ignoreCase = true)) {
                if (cacheOutputStream)
                    OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_ERR)
                else
                    OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR)
            } else if ("System.out".equals(logFile, ignoreCase = true)) {
                if (cacheOutputStream)
                    OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_OUT)
                else
                    OutputChoice(OutputChoice.OutputChoiceType.SYS_OUT)
            } else {
                try {
                    val fos = FileOutputStream(logFile)
                    val printStream = PrintStream(fos)
                    OutputChoice(printStream)
                } catch (var4: FileNotFoundException) {
                    Util.report("Could not open [$logFile]. Defaulting to System.err", var4)
                    OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR)
                }

            }
        }


    }

    internal fun init() {
        val defaultLogLevelString = "info"
        this.defaultLogLevel = stringToLevel(defaultLogLevelString)

        this.showLogName = false
        this.showShortLogName = true
        this.showDateTime = true
        this.showThreadName = false
        dateTimeFormatStr = DATE_TIME_FORMAT_STR_DEFAULT
        this.levelInBrackets = true
        this.warnLevelString = "WARN"
        this.cacheOutputStream = true
        this.outputChoice = computeOutputChoice(this.logFile, this.cacheOutputStream)
        try {
            this.dateFormatter = SimpleDateFormat(dateTimeFormatStr)
        } catch (var3: IllegalArgumentException) {
            Util.report("Bad date format, will output relative time", var3)
        }

    }
}