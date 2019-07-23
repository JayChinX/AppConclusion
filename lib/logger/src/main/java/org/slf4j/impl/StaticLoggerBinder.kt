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
import org.slf4j.spi.LoggerFactoryBinder

/**
 * The binding of [org.slf4j.LoggerFactory] class with an actual instance of
 * [ILoggerFactory] is performed using information returned by this class.
 *
 * @author Ceki Glc
 */
class StaticLoggerBinder private constructor() : LoggerFactoryBinder {

    /** The ILoggerFactory instance returned by the [.getLoggerFactory] method
     * should always be the same object
     */
    private val loggerFactory: ILoggerFactory = SimpleLoggerFactory()

    override fun getLoggerFactory(): ILoggerFactory {
        return loggerFactory
    }

    override fun getLoggerFactoryClassStr(): String {
        return StaticLoggerBinder.loggerFactoryClassStr
    }

    companion object {

        /**
         * The unique instance of this class.
         *
         */
        /**
         * Return the singleton of this class.
         *
         * @return the StaticLoggerBinder singleton
         */
        private val singleton = StaticLoggerBinder()

        @JvmStatic
        fun getSingleton(): StaticLoggerBinder {
            return singleton
        }

        private val loggerFactoryClassStr = SimpleLoggerFactory::class.java.name
    }
}

/**
 * Declare the version of the SLF4J API this implementation is compiled against.
 * The value of this field is usually modified with each release.
 */
// to avoid constant folding by the compiler, this field must *not* be final
var REQUESTED_API_VERSION = "1.6.99" // !final