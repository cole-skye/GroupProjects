import 'package:flutter/material.dart';
import 'package:robot_world/src/Pages/player_home.dart';
import 'package:robot_world/src/controller.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;

class LoadWorld extends StatelessWidget {
  const LoadWorld({Key? key}) : super(key: key);

  List? get dataObject => null;

  /// fetches a list of worlds that currently occupy the database
  Future<List> fetchNames() async {
    var response = await http.get(Uri.parse('http://${Controller.ip.text}:${Controller.port.text}/loadWorld'));
    var data = jsonDecode(response.body);

    print(data.toString());
    return data;

  }

  /// loads the selected the world.
  Future<void> loadWorld(String name) async {
    var response = await http.get(Uri.parse('http://${Controller.ip.text}:${Controller.port.text}/world/$name'));
  }

  /// Displays a list of worlds you can load from the database
  /// When you click on the respected world it is loaded to the app.
  @override
  Widget build(BuildContext context) {

    fetchNames();
    return Scaffold(
      appBar: AppBar(
        title: const Text('Worlds'),
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
                        onTap: (){
                          loadWorld(snapshot.data[i]);
                          Navigator.push(
                              context,
                              MaterialPageRoute(
                              builder: (context) => const PlayerHome(title: 'Robot World',)
                          ));
                        },
                      ),
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
