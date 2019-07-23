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

import org.slf4j.ILoggerFactory
import org.slf4j.Logger

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * SimpleLoggerFactory is an implementation of [ILoggerFactory] returning
 * the appropriately named [SimpleLoggerAdapter] instance.
 *
 * @author Ceki Glc
 */
class SimpleLoggerFactory : ILoggerFactory {

    // key: name (String), value: a SimpleLoggerAdapter;
    private var loggerMap: ConcurrentMap<String, Logger> = ConcurrentHashMap()

    init {
        SimpleLogger.lazyInit()
    }

    /*
     * (non-Javadoc)
     *
     * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
     */
    override fun getLogger(name: String): Logger {
        val name = if (name.equals(Logger.ROOT_LOGGER_NAME, ignoreCase = true)) {
            ""
        } else {
            name
        }

        val slf4jLogger = loggerMap[name]
        return if (slf4jLogger != null)
            slf4jLogger
        else {
//            val julLogger = java.util.logging.Logger.getLogger(name)
//            val newInstance = SimpleLoggerAdapter(julLogger)
            val newInstance = SimpleLogger(name)
            val oldInstance = loggerMap.putIfAbsent(name, newInstance)
            oldInstance ?: newInstance
        }
    }
}
