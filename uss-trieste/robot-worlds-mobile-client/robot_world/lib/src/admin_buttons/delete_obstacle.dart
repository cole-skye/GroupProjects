import 'dart:async';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:robot_world/src/Pages/player_home.dart';
import 'package:robot_world/src/controller.dart';

class DeleteObstacle extends StatefulWidget {
  const DeleteObstacle({Key? key}) : super(key: key);

  @override
  _DelObs createState() => _DelObs();
}

/// Sends an http delete request to the backend api to
/// delete the obstacle list.
class _DelObs extends State<DeleteObstacle> {
  Future sendDeleteObstacles() async {
    final response = await http.delete(
      Uri.parse(
          'http://${Controller.ip.text}:${Controller.port.text}/obstacle'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );
    if (response.statusCode == 200) {
    } else {
      throw Exception('Failed to Send add obstacles to Robot World\'s Server.');
    }
  }

  /// A pop up alert box that asks if you want to "remove obstacles"
  /// prompts two options "cancel" and "yes"
  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text('Remove Obstacles?'),
      actions: <Widget>[
        TextButton(
            onPressed: () => Navigator.pop(context, 'Cancel'),
            child: const Text('No')),
        TextButton(
            onPressed: () {
              sendDeleteObstacles();
              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) =>
                          const PlayerHome(title: "Robot Worlds")));
            },
            child: const Text('Yes')),
      ],
    );
  }
}
