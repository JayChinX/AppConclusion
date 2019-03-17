import 'dart:async';
import 'dart:ui';
import 'package:english_words/english_words.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/other/save.dart';

Color white = const Color(0xFFFFFFFF);//16进制的ARGB
//Color c = const Color.fromARGB(0xFF, 0x42, 0xA5, 0xF5);
//Color c = const Color.fromARGB(255, 66, 165, 245);
//Color c = const Color.fromRGBO(66, 165, 245, 1.0);//opacity：不透明度

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.grey,
        primaryColor: Colors.white
      ),
      home: MyHomePage(title: 'Home'),
      //      home: new RandomWords(),
//      home: new LoginPage(),
//      home: new HomePage(),
      //注册路由表
      routes: {
//        "new_page": (context) => TwoApp("内容被固定"),
      },
    );
  }
}

class MyHomePage extends StatefulWidget {
  final String title;

  MyHomePage({Key key, this.title}) : super(key: key);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class RandomWordsWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final wordPair = new WordPair.random();
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: new Text(wordPair.toString()),
    );
  }
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Flex(direction: Axis.vertical,
            children: <Widget>[
              Text(
                'You have pushed the button this many times:',
              ),
              Text(
                '$_counter',
                style: Theme.of(context).textTheme.display1,
              ),
              FlatButton(
                child: Text("open new router"),
                textColor: Colors.blue,
                onPressed: () {
                  Navigator.push(context, MaterialPageRoute(builder: (context) {
                    return new RandomWords();
                  }),);

//                //通过路由表跳转 传递的参数必须在路由表中固定
//                Navigator.pushNamed(context, "new_page");
                },
              ),
              RandomWordsWidget(),
            ],)
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }
}

//接收android 原生传递过来的参数  window.defaultRouteName
void main() => runApp(_widgetForRoute(window.defaultRouteName));

void collectLog(String line) {
  //收集日志
  print("main-collectLog: $line");
}

void reportErrorAndLog(FlutterErrorDetails details) {
  //上报错误和日志的逻辑
  print("main-reportErrorAndLog: ${details.toString()}");
}

// ignore: missing_return
FlutterErrorDetails makeDetails(Object obj, StackTrace stack) {
  //构建错误信息
  return FlutterErrorDetails(stack: stack, exception: obj);
}

Widget _widgetForRoute(String route) {
  switch (route) {
    case 'route1':
      return MyApp();
    default:
      return MyApp();
  }
}

//void main() {
//  FlutterError.onError = (FlutterErrorDetails details) {
//    reportErrorAndLog(details);
//  };
//  runZoned(() => runApp(_widgetForRoute(window.defaultRouteName)),
//      zoneSpecification: ZoneSpecification(
//          print: (Zone self, ZoneDelegate parent, Zone zone, String line) {
//    collectLog(line);
//  }), onError: (Object obj, StackTrace stack) {
//    var details = makeDetails(obj, stack);
//    reportErrorAndLog(details);
//  });
//} //应用入口main函数
