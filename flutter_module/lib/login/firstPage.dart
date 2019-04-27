import 'package:flutter/material.dart';

class Echo extends StatelessWidget {
  const Echo({
    Key key,
    @required this.text,
    this.backgroundColor: Colors.grey,
  }) : super(key: key);

  final String text;
  final Color backgroundColor;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: text,
      theme: new ThemeData(
        primarySwatch: backgroundColor
      ),
      home: new FirstPage(backgroundColor: Colors.black),
    );
      Center(
      child: Container(
        color: backgroundColor,
        child: Text(text),
      ),
    );
  }
}

class FirstPage extends StatefulWidget {
  final Color backgroundColor;
  const FirstPage({Key key, @required this.backgroundColor}):super(key: key);
  @override
  State<StatefulWidget> createState() => _FirstPage();
  
}

class _FirstPage extends State<FirstPage> {
  bool _active = false;
  
  void _handleTap() {
    setState(() {
      _active = !_active;
    });
  }
  @override
  Widget build(BuildContext context) {
    return new GestureDetector(
      onTap: _handleTap,
      child: new Container(
        child: new Center(
          child: new Text(
            _active ? "Active" : "Inactive",
            style: new TextStyle(fontSize: 32.0, color: widget.backgroundColor),
          ),
        ),
        width: 200.0,
        height: 200.0,
        decoration: new BoxDecoration(
          color: _active ? Colors.lightGreen[700] : Colors.grey[600]
        ),
      ),
    );
  }
  
}
