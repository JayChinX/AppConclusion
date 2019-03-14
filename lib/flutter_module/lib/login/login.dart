import 'package:flutter/material.dart';
import 'package:flutter/services.dart' show rootBundle;
import 'dart:developer';
import 'package:flutter_module/login/firstPage.dart';

//void main() => runApp(new MyApp()); //=>单行函数方法的缩写

//Stateless不可变
class TwoApp extends StatelessWidget {
  //获取参数
  final String tip;

  const TwoApp(this.tip);

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: "我的Flutter" + tip,
      theme: new ThemeData(
        //主题
        primarySwatch: Colors.grey,
      ),
      //首页
      home: new MyHomePage(title: "我的Flutter" + tip),
      routes: {
        "login": (context) => Echo(
              text: "通过路由进入",
            ),
      },
    );
  }
}

//stateful 由StatefulWidget和State两个类组成
//StatefulWidget本身不可变，由State类来保存和改变它的状态
class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title, this.initValue: 1}) : super(key: key);
  final String title;
  final int initValue;

  @override
  State<StatefulWidget> createState() {
    return new _MyHomePageState();
  }
}

//MyHomePage的状态类
class _MyHomePageState extends State<MyHomePage> {
  int _counter;
  bool _userState = false;

  //设置自增函数
  void _incrementCounter() {
    //自增函数
    setState(() {
      //setState 通知flutter框架有状态变化 更新整个StatefulWidget的状态
      _counter++;
      print(_counter);
//      debugPrint("", 1);
//      debugger(when: _counter > 10);//满足条件中断
      if (_counter == 10) {
        Navigator.push(context, new MaterialPageRoute(builder: (context) {
          return _userState ? new Echo(text: "进入first page") : new LoginPage();
        }));
      }
    });
  }

  @override
  void initState() {
    super.initState();
    _counter = widget.initValue;
    print("initState");
  }

  @override
  Widget build(BuildContext context) {
    print("build");
    //在build方法中构建页面widget树
    return new Scaffold(
      //屏幕widget
      appBar: new AppBar(
        title: new Text(widget.title),
      ),
      body: new Center(
        //屏幕中心widget
        child: new Column(
          //从上到下排列的widget
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Text("aaaa"),
            new Text(
              '$_counter',
              style: Theme.of(context).textTheme.display1,
            ),
          ],
        ),
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _incrementCounter, //点击回调
        tooltip: "In",
        child: new Icon(Icons.add),
      ),
    );
  }

  @override
  void didUpdateWidget(MyHomePage oldWidget) {
    super.didUpdateWidget(oldWidget);
    print("didUpdateWidget");
  }

  @override
  void deactivate() {
    super.deactivate();
    print("deactivate");
  }

  @override
  void dispose() {
    super.dispose();
    print("dispose");
  }

  @override
  void reassemble() {
    super.reassemble();
    print("reassemble");
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    print("didChangeDependencies");
  }
}

class LoginPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _LoginPage();
}

class _LoginPage extends State<LoginPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("登录"),
      ),
      body: Column(
        children: <Widget>[
          Text("请登录"),
          Container(
            child: TextField(
              autofocus: true,
              decoration: InputDecoration(
                  labelText: "用户名",
                  hintText: "手机号或邮箱",
                  prefixIcon: Icon(Icons.person_pin),
                  border: InputBorder.none),
            ),
            decoration: BoxDecoration(
                border: Border(
                    bottom: BorderSide(color: Colors.grey[200], width: 0.5))),
          )
        ],
      ),
    );
  }
}
