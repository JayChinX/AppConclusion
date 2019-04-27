import 'dart:async';
import 'dart:ui';
import 'package:english_words/english_words.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/other/save.dart';

Color white = const Color(0xFFFFFFFF); //16进制的ARGB
//Color c = const Color.fromARGB(0xFF, 0x42, 0xA5, 0xF5);
//Color c = const Color.fromARGB(255, 66, 165, 245);
//Color c = const Color.fromRGBO(66, 165, 245, 1.0);//opacity：不透明度

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
//          primarySwatch: Colors.grey,
          primaryColor: Colors.white),
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
            Flex(
              direction: Axis.vertical,
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
                    //可以动态传递参数
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) {
                        return new RandomWords();
                      }),
                    );
                    /**
                     * MaterialApp(
                     *  routes: {
                     *          "new_page": (context) => TwoApp("内容被固定"),
                     *  },)
                     *
                     * Navigator:
                     *  1.通过路由表跳转 传递的参数必须在路由表中固定
                     *      Navigator.pushNamed(context, "new_page").then((value) {
                     *         //返回值 value
                     *       });
                     *       Navigator.push(context, MaterialPageRoute(builder: (context) {return new RandomWords();}),);
                     *  2.pushReplacement方法进入screen3页面后，此页面会执行dispose方法销毁，
                     *      在screen3返回会直接进入此页面的上一个页面
                     *      Navigator.pushReplacement( context, MaterialPageRoute(builder: (BuildContext context) => screen4()));
                     *      Navigator.of(context).pushReplacementNamed('/screen3');
                     *  3.popAndPushNamed  返回到一个存在的页面，并且pop当前页面并传递参数
                     *      Navigator.popAndPushNamed(context, '/screen4');
                     *  4.pushNamedAndRemoveUntil将指定的页面加入路由并且把其他所有页面pop销毁掉
                     *      Navigator.of(context).pushNamedAndRemoveUntil('/screen4', (Route<dynamic> route) => false);
                     *  5.pushNamedAndRemoveUntil将指定的页面加入路由，并且pop掉和screen1之间的所有页面
                     *      Navigator.pushAndRemoveUntil(context,MaterialPageRoute(builder: (BuildContext context) => new  screen4()),ModalRoute.withName('/'),
                     *      Navigator.of(context).pushNamedAndRemoveUntil('/screen4', ModalRoute.withName('/screen1'));
                     *      举例：
                     *         1)1-->2-->3,3到4时使用Navigator.pushNamedAndRemoveUntil(context,"/screen4",ModalRoute.withName('/screen1'));
                     *           这时候如果在页面4点击返回，将会直接退出程序。
                     *         2)1-->2-->3,3到4时使用Navigator.pushNamedAndRemoveUntil(context,"/screen4",ModalRoute.withName('/'));
                     *           这时候如果在页面4点击返回，将会直接回到页面1。
                     *         3)1-->2-->1-->2-->3,3到4时使用Navigator.pushNamedAndRemoveUntil(context,"/screen4",ModalRoute.withName('/screen1'));
                     *           这时候如果在页面4点击返回，将会回到第二个1页面。
                     *
                     * Pop:
                     *  1.Navigator.of(context).pop();
                     *      直接退出当前页面
                     *  2.Navigator.of(context).maybePop();
                     *      判断如果退出当前页面后出现其他页面不会出现问题就执行，否则不执行
                     *  3.Navigator.of(context).canPop();
                     *      判断当前页面能否执行pop操作，并返回bool
                     *
                     * 参数的传递也接收：
                     * Navigator.of(context).pop(“传递的参数”)
                     * Navigator.pushNamed(context, "new_page").then((value) {
                     *         //接收的返回值 value
                     *       });
                     */
                  },
                ),
                RandomWordsWidget(),
              ],
            )
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
