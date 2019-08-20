//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.slf4j.impl;

import android.util.Log;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class AndroidLogger extends MarkerIgnoringBase {
    private static final long serialVersionUID = -1227274521521287937L;

    AndroidLogger(String name) {
        this.name = name;
    }

    public boolean isTraceEnabled() {
        return Log.isLoggable(this.name, 2);
    }

    public void trace(String msg) {
        Log.v(this.name, msg);
    }

    public void trace(String format, Object param1) {
        Log.v(this.name, this.format(format, param1, (Object)null));
    }

    public void trace(String format, Object param1, Object param2) {
        Log.v(this.name, this.format(format, param1, param2));
    }

    public void trace(String format, Object[] argArray) {
        Log.v(this.name, this.format(format, argArray));
    }

    public void trace(String msg, Throwable t) {
        Log.v(this.name, msg, t);
    }

    public boolean isDebugEnabled() {
        return Log.isLoggable(this.name, 3);
    }

    public void debug(String msg) {
        Log.d(this.name, msg);
    }

    public void debug(String format, Object arg1) {
        Log.d(this.name, this.format(format, arg1, (Object)null));
    }

    public void debug(String format, Object param1, Object param2) {
        Log.d(this.name, this.format(format, param1, param2));
    }

    public void debug(String format, Object[] argArray) {
        Log.d(this.name, this.format(format, argArray));
    }

    public void debug(String msg, Throwable t) {
        Log.d(this.name, msg, t);
    }

    public boolean isInfoEnabled() {
        return Log.isLoggable(this.name, 4);
    }

    public void info(String msg) {
        Log.i(this.name, msg);
    }

    public void info(String format, Object arg) {
        Log.i(this.name, this.format(format, arg, (Object)null));
    }

    public void info(String format, Object arg1, Object arg2) {
        Log.i(this.name, this.format(format, arg1, arg2));
    }

    public void info(String format, Object[] argArray) {
        Log.i(this.name, this.format(format, argArray));
    }

    public void info(String msg, Throwable t) {
        Log.i(this.name, msg, t);
    }

    public boolean isWarnEnabled() {
        return Log.isLoggable(this.name, 5);
    }

    public void warn(String msg) {
        Log.w(this.name, msg);
    }

    public void warn(String format, Object arg) {
        Log.w(this.name, this.format(format, arg, (Object)null));
    }

    public void warn(String format, Object arg1, Object arg2) {
        Log.w(this.name, this.format(format, arg1, arg2));
    }

    public void warn(String format, Object[] argArray) {
        Log.w(this.name, this.format(format, argArray));
    }

    public void warn(String msg, Throwable t) {
        Log.w(this.name, msg, t);
    }

    public boolean isErrorEnabled() {
        return Log.isLoggable(this.name, 6);
    }

    public void error(String msg) {
        Log.e(this.name, msg);
    }

    public void error(String format, Object arg) {
        Log.e(this.name, this.format(format, arg, (Object)null));
    }

    public void error(String format, Object arg1, Object arg2) {
        Log.e(this.name, this.format(format, arg1, arg2));
    }

    public void error(String format, Object[] argArray) {
        Log.e(this.name, this.format(format, argArray));
    }

    public void error(String msg, Throwable t) {
        Log.e(this.name, msg, t);
    }

    private String format(String format, Object arg1, Object arg2) {
        return MessageFormatter.format(format, arg1, arg2).getMessage();
    }

    private String format(String format, Object[] args) {
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }
}
