//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.slf4j.impl

import android.util.Log
import java.util.HashMap
import java.util.StringTokenizer
import org.slf4j.ILoggerFactory
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class AndroidLoggerFactory : ILoggerFactory {
    private var loggerMap: ConcurrentMap<String, Logger> = ConcurrentHashMap()

    override fun getLogger(name: String): Logger {
        val name = if (name.equals(Logger.ROOT_LOGGER_NAME, ignoreCase = true)) {
            ""
        } else {
            name
        }

        val actualName = this.forceValidName(name)


        val slf4jLogger = loggerMap[actualName]
        return if (slf4jLogger != null)
            slf4jLogger
        else {
            if (actualName != name) {
                Log.i(
                    AndroidLoggerFactory::class.java.simpleName,
                    "Logger name '$name' exceeds maximum length of $TAG_MAX_LENGTH characters, using '$actualName' instead."
                )
            }
            val newInstance = AndroidLogger(name)
            val oldInstance = loggerMap.putIfAbsent(name, newInstance)
            oldInstance ?: newInstance
        }
    }

    private fun forceValidName(name: String?): String? {
        var name = name
        if (name != null && name.length > TAG_MAX_LENGTH) {
            val st = StringTokenizer(name, ".")
            if (st.hasMoreTokens()) {
                val sb = StringBuilder()

                while (true) {
                    val token = st.nextToken()
                    if (token.length == 1) {
                        sb.append(token)
                        sb.append('.')
                    } else if (st.hasMoreTokens()) {
                        sb.append(token[0])
                        sb.append("*.")
                    } else {
                        sb.append(token)
                    }

                    if (!st.hasMoreTokens()) {
                        name = sb.toString()
                        break
                    }
                }
            }

            if (name!!.length > TAG_MAX_LENGTH) {
                name = name.substring(0, 22) + '*'
            }
        }

        return name
    }

    companion object {
        internal val TAG_MAX_LENGTH = 23
    }
}
