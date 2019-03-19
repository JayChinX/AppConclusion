import 'dart:async';
import 'package:flutter/services.dart';

/*
 * 1.MethodChannel
 */

Future sendNativeMessage() async {
  /**
   * 1.com.simple.channelflutterandroid/method 可以不是包名 对应一致
   * 2.StandardMethodCodec(): 非必传，默认为StandardMethodCodec 一种信息编码器
   *    1)对于 MethodChannel 和 EventChannel 两种方式，有两种编解码器，均实现自MethodCodec ，
   *    分别为 StandardMethodCodec 和 JsonMethodCodec。
   *    2)对于 BasicMessageChannel 方式，有四种编解码器，均实现自MessageCodec ，
   *    分别为 StandardMessageCodec、JSONMessageCodec、StringCodec和BinaryCodec。
   */
  var result = await MethodChannel("com.simple.channelflutterandroid/method", StandardMethodCodec())
      .invokeMethod("toast", {"msg": ""});
  return result;
}

/*
 * 2.EventChannel
 */
Future getEventNativeMessage() async {
  EventChannel("com.simple.channelflutterandroid/event").receiveBroadcastStream().listen((event) {
    //接收到的消息
    print(event.toString());
  });
}

/*
 * 3.BasicMessageChannel
 */
var _basicMessageChannel = BasicMessageChannel("com.simple.channelflutterandroid/basic", StringCodec());
//发送消息
void sendBasicMessage() {
  _basicMessageChannel.send("");
}

void getBasicNativeMessage() {
  _basicMessageChannel.setMessageHandler((str) {
    //接收到的消息

  });
}
/*
 * 3.BasicMessageChannel - binary
 */
void send() async {
  //发送
  var res = await BinaryMessages.send("com.simple.channelflutterandroid/basic/binary", ByteData(225));
  //接收
  BinaryMessages.setMessageHandler("com.simple.channelflutterandroid/basic/binary", (byteData) {
    //接收到的消息
  });
}