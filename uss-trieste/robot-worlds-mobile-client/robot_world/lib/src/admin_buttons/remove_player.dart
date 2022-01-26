import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:robot_world/src/Pages/player_home.dart';
import 'package:robot_world/src/controller.dart';

class RemovePlayer extends StatelessWidget {
  const RemovePlayer({Key? key}) : super(key: key);

  List? get dataObject => null;

  /// Fetches a list of robots from the server.
  Future<List<String>> fetchNames() async {
    List<String> names = [];
    var response = await http.get(Uri.parse(
        'http://${Controller.ip.text}:${Controller.port.text}/robot'));
    var data = jsonDecode(response.body);
    for (var name in data["robots"]) {
      names.add(name["name"]);
    }
    return names;
  }

  /// Sends a http delete request attached with the robots name you would like
  /// to delete
  Future sendPurgeCommand(String name) async {
    final response = await http.delete(
      Uri.parse(
          'http://${Controller.ip.text}:${Controller.port.text}/robot/$name'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    if (response.statusCode == 200) {
    } else {
      throw Exception('Failed to Send purge to Robot World\'s Server.');
    }
  }

  /// A list of robots that occupy the world with a trailing icon button
  /// of a bin to delete the selected robot.
  @override
  Widget build(BuildContext context) {
    fetchNames();
    return Scaffold(
      appBar: AppBar(
        title: const Text('Robots'),
      ),
      body: Center(
        child: FutureBuilder(
          future: fetchNames(),
          builder: (context, AsyncSnapshot snapshot) {
            return ListView.builder(
                itemCount: snapshot.data.length,
                itemBuilder: (context, i) {
                  if (snapshot.hasData) {
                    return Card(
                      child: ListTile(
                          title: Text(snapshot.data[i]),
                          leading: const Icon(Icons.arrow_right),
                          trailing: IconButton(
                            icon: const Icon(Icons.delete),
                            onPressed: () {
                              sendPurgeCommand(snapshot.data[i]);
                              Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                      builder: (context) => const PlayerHome(
                                          title: "Robot Worlds")));
                            },
                          )),
                    );
                  } else if (snapshot.hasError) {
                    return Text('${snapshot.error}');
                  }
                  return const CircularProgressIndicator();
                });
          }, // builder
        ),
      ),
    );
  }
}
