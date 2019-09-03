/**
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.slf4j.impl

import org.slf4j.Marker
import org.slf4j.helpers.MarkerIgnoringBase
import org.slf4j.helpers.MessageFormatter
import org.slf4j.spi.LocationAwareLogger
import java.util.logging.Level
import java.util.logging.LogRecord

// WARN: SimpleLoggerAdapter constructor should have only package access so
// that only SimpleLoggerFactory be able to create one.
class SimpleLoggerAdapter internal constructor(val logger: java.util.logging.Logger) : MarkerIgnoringBase(), LocationAwareLogger {

    init {
        this.name = logger.name
    }

    /**
     * Is this logger instance enabled for the FINEST level?
     *
     * @return True if this Logger is enabled for level FINEST, false otherwise.
     */
    override fun isTraceEnabled(): Boolean {
        return logger.isLoggable(Level.FINEST)
    }

    /**
     * Log a message object at level FINEST.
     *
     * @param msg
     * - the message object to be logged
     */
    override fun trace(msg: String) {
        if (logger.isLoggable(Level.FINEST)) {
            log(SELF, Level.FINEST, msg, null)
        }
    }

    /**
     * Log a message at level FINEST according to the specified format and
     * argument.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for level FINEST.
     *
     *
     * @param format
     * the format string
     * @param arg
     * the argument
     */
    override fun trace(format: String, arg: Any) {
        if (logger.isLoggable(Level.FINEST)) {
            val ft = MessageFormatter.format(format, arg)
            log(SELF, Level.FINEST, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at level FINEST according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the FINEST level.
     *
     *
     * @param format
     * the format string
     * @param arg1
     * the first argument
     * @param arg2
     * the second argument
     */
    override fun trace(format: String, arg1: Any, arg2: Any) {
        if (logger.isLoggable(Level.FINEST)) {
            val ft = MessageFormatter.format(format, arg1, arg2)
            log(SELF, Level.FINEST, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at level FINEST according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the FINEST level.
     *
     *
     * @param format
     * the format string
     * @param argArray
     * an array of arguments
     */
    override fun trace(format: String, vararg argArray: Any) {
        if (logger.isLoggable(Level.FINEST)) {
            val ft = MessageFormatter.arrayFormat(format, argArray)
            log(SELF, Level.FINEST, ft.message, ft.throwable)
        }
    }

    /**
     * Log an exception (throwable) at level FINEST with an accompanying message.
     *
     * @param msg
     * the message accompanying the exception
     * @param t
     * the exception (throwable) to log
     */
    override fun trace(msg: String, t: Throwable) {
        if (logger.isLoggable(Level.FINEST)) {
            log(SELF, Level.FINEST, msg, t)
        }
    }

    /**
     * Is this logger instance enabled for the FINE level?
     *
     * @return True if this Logger is enabled for level FINE, false otherwise.
     */
    override fun isDebugEnabled(): Boolean {
        return logger.isLoggable(Level.FINE)
    }

    /**
     * Log a message object at level FINE.
     *
     * @param msg
     * - the message object to be logged
     */
    override fun debug(msg: String) {
        if (logger.isLoggable(Level.FINE)) {
            log(SELF, Level.FINE, msg, null)
        }
    }

    /**
     * Log a message at level FINE according to the specified format and argument.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for level FINE.
     *
     *
     * @param format
     * the format string
     * @param arg
     * the argument
     */
    override fun debug(format: String, arg: Any) {
        if (logger.isLoggable(Level.FINE)) {
            val ft = MessageFormatter.format(format, arg)
            log(SELF, Level.FINE, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at level FINE according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the FINE level.
     *
     *
     * @param format
     * the format string
     * @param arg1
     * the first argument
     * @param arg2
     * the second argument
     */
    override fun debug(format: String, arg1: Any, arg2: Any) {
        if (logger.isLoggable(Level.FINE)) {
            val ft = MessageFormatter.format(format, arg1, arg2)
            log(SELF, Level.FINE, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at level FINE according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the FINE level.
     *
     *
     * @param format
     * the format string
     * @param argArray
     * an array of arguments
     */
    override fun debug(format: String, vararg argArray: Any) {
        if (logger.isLoggable(Level.FINE)) {
            val ft = MessageFormatter.arrayFormat(format, argArray)
            log(SELF, Level.FINE, ft.message, ft.throwable)
        }
    }

    /**
     * Log an exception (throwable) at level FINE with an accompanying message.
     *
     * @param msg
     * the message accompanying the exception
     * @param t
     * the exception (throwable) to log
     */
    override fun debug(msg: String, t: Throwable) {
        if (logger.isLoggable(Level.FINE)) {
            log(SELF, Level.FINE, msg, t)
        }
    }

    /**
     * Is this logger instance enabled for the INFO level?
     *
     * @return True if this Logger is enabled for the INFO level, false otherwise.
     */
    override fun isInfoEnabled(): Boolean {
        return logger.isLoggable(Level.INFO)
    }

    /**
     * Log a message object at the INFO level.
     *
     * @param msg
     * - the message object to be logged
     */
    override fun info(msg: String) {
        if (logger.isLoggable(Level.INFO)) {
            log(SELF, Level.INFO, msg, null)
        }
    }

    /**
     * Log a message at level INFO according to the specified format and argument.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the INFO level.
     *
     *
     * @param format
     * the format string
     * @param arg
     * the argument
     */
    override fun info(format: String, arg: Any) {
        if (logger.isLoggable(Level.INFO)) {
            val ft = MessageFormatter.format(format, arg)
            log(SELF, Level.INFO, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at the INFO level according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the INFO level.
     *
     *
     * @param format
     * the format string
     * @param arg1
     * the first argument
     * @param arg2
     * the second argument
     */
    override fun info(format: String, arg1: Any, arg2: Any) {
        if (logger.isLoggable(Level.INFO)) {
            val ft = MessageFormatter.format(format, arg1, arg2)
            log(SELF, Level.INFO, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at level INFO according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the INFO level.
     *
     *
     * @param format
     * the format string
     * @param argArray
     * an array of arguments
     */
    override fun info(format: String, vararg argArray: Any) {
        if (logger.isLoggable(Level.INFO)) {
            val ft = MessageFormatter.arrayFormat(format, argArray)
            log(SELF, Level.INFO, ft.message, ft.throwable)
        }
    }

    /**
     * Log an exception (throwable) at the INFO level with an accompanying
     * message.
     *
     * @param msg
     * the message accompanying the exception
     * @param t
     * the exception (throwable) to log
     */
    override fun info(msg: String, t: Throwable) {
        if (logger.isLoggable(Level.INFO)) {
            log(SELF, Level.INFO, msg, t)
        }
    }

    /**
     * Is this logger instance enabled for the WARNING level?
     *
     * @return True if this Logger is enabled for the WARNING level, false
     * otherwise.
     */
    override fun isWarnEnabled(): Boolean {
        return logger.isLoggable(Level.WARNING)
    }

    /**
     * Log a message object at the WARNING level.
     *
     * @param msg
     * - the message object to be logged
     */
    override fun warn(msg: String) {
        if (logger.isLoggable(Level.WARNING)) {
            log(SELF, Level.WARNING, msg, null)
        }
    }

    /**
     * Log a message at the WARNING level according to the specified format and
     * argument.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the WARNING level.
     *
     *
     * @param format
     * the format string
     * @param arg
     * the argument
     */
    override fun warn(format: String, arg: Any) {
        if (logger.isLoggable(Level.WARNING)) {
            val ft = MessageFormatter.format(format, arg)
            log(SELF, Level.WARNING, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at the WARNING level according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the WARNING level.
     *
     *
     * @param format
     * the format string
     * @param arg1
     * the first argument
     * @param arg2
     * the second argument
     */
    override fun warn(format: String, arg1: Any, arg2: Any) {
        if (logger.isLoggable(Level.WARNING)) {
            val ft = MessageFormatter.format(format, arg1, arg2)
            log(SELF, Level.WARNING, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at level WARNING according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the WARNING level.
     *
     *
     * @param format
     * the format string
     * @param argArray
     * an array of arguments
     */
    override fun warn(format: String, vararg argArray: Any) {
        if (logger.isLoggable(Level.WARNING)) {
            val ft = MessageFormatter.arrayFormat(format, argArray)
            log(SELF, Level.WARNING, ft.message, ft.throwable)
        }
    }

    /**
     * Log an exception (throwable) at the WARNING level with an accompanying
     * message.
     *
     * @param msg
     * the message accompanying the exception
     * @param t
     * the exception (throwable) to log
     */
    override fun warn(msg: String, t: Throwable) {
        if (logger.isLoggable(Level.WARNING)) {
            log(SELF, Level.WARNING, msg, t)
        }
    }

    /**
     * Is this logger instance enabled for level SEVERE?
     *
     * @return True if this Logger is enabled for level SEVERE, false otherwise.
     */
    override fun isErrorEnabled(): Boolean {
        return logger.isLoggable(Level.SEVERE)
    }

    /**
     * Log a message object at the SEVERE level.
     *
     * @param msg
     * - the message object to be logged
     */
    override fun error(msg: String) {
        if (logger.isLoggable(Level.SEVERE)) {
            log(SELF, Level.SEVERE, msg, null)
        }
    }

    /**
     * Log a message at the SEVERE level according to the specified format and
     * argument.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the SEVERE level.
     *
     *
     * @param format
     * the format string
     * @param arg
     * the argument
     */
    override fun error(format: String, arg: Any) {
        if (logger.isLoggable(Level.SEVERE)) {
            val ft = MessageFormatter.format(format, arg)
            log(SELF, Level.SEVERE, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at the SEVERE level according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the SEVERE level.
     *
     *
     * @param format
     * the format string
     * @param arg1
     * the first argument
     * @param arg2
     * the second argument
     */
    override fun error(format: String, arg1: Any, arg2: Any) {
        if (logger.isLoggable(Level.SEVERE)) {
            val ft = MessageFormatter.format(format, arg1, arg2)
            log(SELF, Level.SEVERE, ft.message, ft.throwable)
        }
    }

    /**
     * Log a message at level SEVERE according to the specified format and
     * arguments.
     *
     *
     *
     * This form avoids superfluous object creation when the logger is disabled
     * for the SEVERE level.
     *
     *
     * @param format
     * the format string
     * @param arguments
     * an array of arguments
     */
    override fun error(format: String, vararg arguments: Any) {
        if (logger.isLoggable(Level.SEVERE)) {
            val ft = MessageFormatter.arrayFormat(format, arguments)
            log(SELF, Level.SEVERE, ft.message, ft.throwable)
        }
    }

    /**
     * Log an exception (throwable) at the SEVERE level with an accompanying
     * message.
     *
     * @param msg
     * the message accompanying the exception
     * @param t
     * the exception (throwable) to log
     */
    override fun error(msg: String, t: Throwable) {
        if (logger.isLoggable(Level.SEVERE)) {
            log(SELF, Level.SEVERE, msg, t)
        }
    }

    /**
     * Log the message at the specified level with the specified throwable if any.
     * This method creates a LogRecord and fills in caller date before calling
     * this instance's JDK14 logger.
     *
     * See bug report #13 for more details.
     *
     * @param level
     * @param msg
     * @param t
     */
    private fun log(callerFQCN: String, level: Level, msg: String, t: Throwable?) {
        // millis and thread are filled by the constructor
        val record = LogRecord(level, msg)
        record.loggerName = getName()
        record.thrown = t
        fillCallerData(callerFQCN, record)
        logger.log(record)

    }

    /**
     * Fill in caller data if possible.
     *
     * @param record
     * The record to update
     */
    private fun fillCallerData(callerFQCN: String, record: LogRecord) {
        val steArray = Throwable().stackTrace

        var selfIndex = -1
        for (i in steArray.indices) {
            val className = steArray[i].className
            if (className == callerFQCN || className == SUPER) {
                selfIndex = i
                break
            }
        }

        var found = -1
        for (i in selfIndex + 1 until steArray.size) {
            val className = steArray[i].className
            if (!(className == callerFQCN || className == SUPER)) {
                found = i
                break
            }
        }

        if (found != -1) {
            val ste = steArray[found]
            // setting the class name has the side effect of setting
            // the needToInferCaller variable to false.
            record.sourceClassName = ste.className
            record.sourceMethodName = ste.methodName
        }
    }

    override fun log(marker: Marker, callerFQCN: String, level: Int, message: String, argArray: Array<Any>, t: Throwable) {
        val julLevel: Level = when (level) {
            LocationAwareLogger.TRACE_INT -> Level.FINEST
            LocationAwareLogger.DEBUG_INT -> Level.FINE
            LocationAwareLogger.INFO_INT -> Level.INFO
            LocationAwareLogger.WARN_INT -> Level.WARNING
            LocationAwareLogger.ERROR_INT -> Level.SEVERE
            else -> throw IllegalStateException("Level number $level is not recognized.")
        }
        // the logger.isLoggable check avoids the unconditional
        // construction of location data for disabled log
        // statements. As of 2008-07-31, callers of this method
        // do not perform this check. See also
        // http://bugzilla.slf4j.org/show_bug.cgi?id=90
        if (logger.isLoggable(julLevel)) {
            log(callerFQCN, julLevel, message, t)
        }
    }

    companion object {

        private const val serialVersionUID = -8053026990503422791L

        internal var SELF = SimpleLoggerAdapter::class.java.name
        internal var SUPER = MarkerIgnoringBase::class.java.name
    }
}
