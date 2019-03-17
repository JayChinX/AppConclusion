import 'package:english_words/english_words.dart';
import 'package:flutter/material.dart';

class RandomWords extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _RandomWordsState();
}

class _RandomWordsState extends State<RandomWords> {
  final List<WordPair> _suggestions = <WordPair>[];
  final Set<WordPair> _saved = new Set<WordPair>();
  final TextStyle _biggerFont = const TextStyle(fontSize: 18.0);

  //listView的widget
  Widget _buildSuggestions() {
    return ListView.builder(
      itemBuilder: (BuildContext _context, int i) {
        if (i.isOdd) {
          //i如果是奇数
          return new Divider();
        }

        final int index = i ~/ 2;
        print('i: $i, index: $index');
        if (index >= _suggestions.length) {
          _suggestions.addAll(generateWordPairs().take(10));
        }
        return _buildRow(index, _suggestions[index]);
      },
      padding: const EdgeInsets.all(16.0),
    );
  }

  //每一行的widget
  Widget _buildRow(int index, WordPair pair) {
    final bool _alreadySaved = _saved.contains(pair);
    return new ListTile(
      title: new Text(
        '$index ${pair.asPascalCase}',
        style: _biggerFont,
      ),
      trailing: new Icon(
        _alreadySaved ? Icons.favorite : Icons.favorite_border,
        color: _alreadySaved ? Colors.red : null,
      ),
      onTap: () {
        setState(() {
          if (_alreadySaved)
            _saved.remove(pair);
          else
            _saved.add(pair);
        });
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('Startup Name Generator'),
        actions: <Widget>[
          new IconButton(icon: const Icon(Icons.list), onPressed: _pushSaved)
        ],
      ),
      body: _buildSuggestions(),
    );
  }

  void _pushSaved() {
    Navigator.of(context).push(
      new MaterialPageRoute(builder: (BuildContext context) {
        final Iterable<ListTile> tiles = _saved.map((WordPair pair) {
          return new ListTile(
            title: Text(
              pair.asPascalCase,
              style: _biggerFont,
            ),
          );
        });
        final List<Widget> divided =
        ListTile.divideTiles(tiles: tiles, context: context).toList();
        return new Scaffold(
          appBar: AppBar(
            title: const Text('Saved'),
          ),
          body: new ListView(
            children: divided,
          ),
        );
      }),
    );
  }
}