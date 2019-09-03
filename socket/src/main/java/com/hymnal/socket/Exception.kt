package com.hymnal.socket

/**
 * 创建连接异常，或者链接客户端异常  之后进行重连
 */
class ConnectorException(message: String) : RuntimeException(message)

/**
 * 长链接异常   之后报异常提醒
 */
class SocketException(message: String): RuntimeException(message)