import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';

final dummySnapshot = [
  {"name": "Filip", "votes": 10},
  {"name": "Abraham", "votes": 10},
  {"name": "Richard", "votes": 10},
  {"name": "Ike", "votes": 10},
  {"name": "Justin", "votes": 10}
];

class HomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _HomePage();
}

class _HomePage extends State<HomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Baby name votes'),
      ),
      body: _buildBody(context),
    );
  }
}

Widget _buildBody(BuildContext context) {
  return _buildList(context, dummySnapshot);
}

Widget _buildList(BuildContext context, List<Map> snapshot) {
  return ListView(
      padding: const EdgeInsets.only(top: 20.0),
      children: snapshot.map((data) => _buildListItem(context, data)).toList());
}

Widget _buildListItem(BuildContext context, Map data) {
  final record = Record.fromMap(data);
  return Padding(
    key: ValueKey(record.name),
    padding: const EdgeInsets.symmetric(horizontal: 18.0, vertical: 10.0),
    child: Container(
      decoration: BoxDecoration(
          border: Border.all(color: Colors.grey),
          borderRadius: BorderRadius.circular(5.0)),
      child: ListTile(
        title: Text(record.name),
        trailing: Text(record.votes.toString()),
        onTap: () => print(record),
      ),
    ),
  );
}

class Record {
  final String name;
  final int votes;
  final DocumentReference reference;

  Record.fromMap(Map<String, dynamic> map, {this.reference})
      : assert(map['name'] != null),
        assert(map['votes'] != null),
        name = map['name'],
        votes = map['votes'];

  Record.fromSnapshot(DocumentSnapshot snapshot)
      : this.fromMap(snapshot.data, reference: snapshot.reference);

  @override
  String toString() => 'Record: <$name:$votes>';
}
