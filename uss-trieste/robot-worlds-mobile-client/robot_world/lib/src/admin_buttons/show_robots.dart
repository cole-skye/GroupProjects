import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:robot_world/src/controller.dart';

class ShowRobots extends StatelessWidget {
  const ShowRobots({Key? key}) : super(key: key);

  List? get dataObject => null;

  ///Fetches a list of robots from the server.
  Future<List> fetchRobots() async {
    List robots = [];
    var response = await http.get(Uri.parse(
        'http://${Controller.ip.text}:${Controller.port.text}/robot'));
    var data = jsonDecode(response.body);
    for (var name in data["robots"]) {
      robots.add(name);
    }

    return robots;
  }

  /// Displays the robots that currently occupy the world.
  /// when the robot name is tapped on it displays the robots status.
  @override
  Widget build(BuildContext context) {
    fetchRobots();
    return Scaffold(
      appBar: AppBar(
        title: const Text('Robots'),
      ),
      body: Center(
        child: FutureBuilder(
          future: fetchRobots(),
          builder: (context, AsyncSnapshot snapshot) {
            return ListView.builder(
                itemCount: snapshot.data.length,
                itemBuilder: (context, i) {
                  if (snapshot.hasData) {
                    return Card(
                      child: ListTile(
                        title: Text(snapshot.data[i]["name"]),
                        leading: const Icon(Icons.arrow_right),
                        onTap: () {
                          showDialog(
                              context: context,
                              builder: (context) => AlertDialog(
                                    title: const Text('Robot'),
                                    actions: <Widget>[
                                      Card(
                                        child: ListTile(
                                            onTap: () => Navigator.pop(context),
                                            title: Text(
                                                '\n Position: ${snapshot.data[i]["position"]}\n'
                                                ' Direction: ${snapshot.data[i]["currentDirection"]}\n'
                                                ' Alive: ${snapshot.data[i]["alive"]}\n'
                                                ' Status: ${snapshot.data[i]["status"]}\n'
                                                ' Shields: ${snapshot.data[i]["shields"]}\n'
                                                ' Shots: ${snapshot.data[i]["shots"]}\n'
                                                ' Mines: ${snapshot.data[i]["numberOfMines"]}\n')),
                                      )
                                    ],
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
