import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:robot_world/src/controller.dart';

class SaveWorld extends StatefulWidget {
  const SaveWorld({Key? key}) : super(key: key);

  @override
  _Save createState() => _Save();
}
/// new world name is stored here.
class _Save extends State<SaveWorld> {
  final worldName = TextEditingController();

  /// clears the variable in case a the variable is occupied
  void clear() {
    worldName.clear();
  }

  /// sends a http post request to the server to save the current world
  Future sendAddCommand() async {

    final response = await http.post(

      Uri.parse('http://${Controller.ip.text}:${Controller.port.text}/world'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'name': worldName.text
      }),
    );

    if (response.statusCode == 200) {

    } else {
      throw Exception('Failed to Send save world to Robot World\'s Server.');
    }
  }

  /// Alert box pop up to insert a new world name and save it.
  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Save World'),
      actions: <Widget>[
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 8),
          child: TextFormField(
            controller: worldName,
            decoration: const InputDecoration(
              border: UnderlineInputBorder(),
              hintText: 'World name',
            ),
          ),
        ),
        TextButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text('Cancel')),
        TextButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text('Confirm')),
      ],
    );
  }
}
