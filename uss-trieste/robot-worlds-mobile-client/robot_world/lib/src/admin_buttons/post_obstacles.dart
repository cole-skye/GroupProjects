import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:robot_world/src/Pages/player_home.dart';

import 'package:robot_world/src/controller.dart';

class AddObstacle extends StatefulWidget {
  const AddObstacle({Key? key}) : super(key: key);

  @override
  _AddObs createState() => _AddObs();
}
/// x and y variables are stored here
class _AddObs extends State<AddObstacle> {
  final xController = TextEditingController();
  final yController = TextEditingController();

  /// every time this page is called the variables are cleared
  /// making sure they are empty before assigned a value.
  void clear() {
    yController.clear();
    xController.clear();
  }

  /// Sends a http post request to add an obstacle to the current world
  /// has an 'x' and 'y' body
  Future sendAddCommand() async {

    final response = await http.post(

      Uri.parse('http://${Controller.ip.text}:${Controller.port.text}/obstacle'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'x': xController.text,
        'y': yController.text
      }),
    );

    if (response.statusCode == 200) {
      //worldIs = fetch//worldData();
      // return //worldData;
    } else {
      throw Exception('Failed to Send add obstacles to Robot World\'s Server.');
    }
  }

  /// An alert pop up box that prompts you to insert a new x and y co-ordinate
  /// for a new obstacle to insert into the world.
  /// has a 'cancel' and 'add' button
  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Add Obstacle'),
      actions: <Widget>[
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 8),
          child: TextFormField(
            controller: xController,
            decoration: const InputDecoration(
              border: UnderlineInputBorder(),
              hintText: 'x co-ordinate',
            ),
          ),
        ),
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 8),
          child: TextFormField(
            controller: yController,
            decoration: const InputDecoration(
              border: UnderlineInputBorder(),
              hintText: 'y co-ordinate',
            ),
          ),
        ),
        TextButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text('Cancel')),
        TextButton(
            onPressed: () {
              sendAddCommand();
              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => const PlayerHome(
                          title: "Robot Worlds")));
            },
            child: const Text('Add')),
      ],
    );
  }
}