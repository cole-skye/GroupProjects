import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:mvc_application/controller.dart';

import 'model.dart';

/// Acts the middle-man between the model class and view class.
/// Handles all HTTP request to the HTTP Server
class Controller extends ControllerMVC {
  factory Controller() => _this ??= Controller._();

  Controller._() {
    model = Model();
  }

  static Controller? _this;
  late final Model model;

  final TextEditingController nameController = TextEditingController();
  final TextEditingController textController = TextEditingController();
  static final name = TextEditingController();
  static final ip = TextEditingController();
  static final port = TextEditingController();

  get messages => model.messages;

  Future get response => model.response;

  get text => textController.text;

  get data => model.data;

  set dataIs(value) {
    model.dataIs = value;
  }

  /// Fetches the robot world data from the HTTP Server.
  /// Updates the map of data held in the Model.
  /// Notifies the any listeners on the changes made to the data map.
  Future<void> fetchWorldData(
      TextEditingController ip, TextEditingController port) async {
    var response =
        await http.get(Uri.parse('http://${ip.text}:${port.text}/world'));

    if (response.statusCode == 200) {
      dataIs = jsonDecode(response.body);
      notifyListeners();
    } else {
      throw Exception('Failed to retrieve current world data.');
    }
  }

  /// Sends a "launch" command request to the HTTP Server.
  Future launch(String name) async {
    final response = await http.post(
      Uri.parse('http://${ip.text}:${port.text}/robot/$name'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
    );

    if (!(response.statusCode == 201)) {
      throw Exception(
          'Failed to Send "launch" command to Robot World\'s Server.');
    }
  }

  /// Sends a "forward 1" command request to the HTTP Server.
  Future sendForwardCommand(String name) async {
    String command = "forward \"1\"";
    Map outgoing = _processRequest(command, name);

    final response = await http.post(
      Uri.parse('http://${ip.text}:${port.text}/command'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'robot': outgoing["robot"],
        'command': outgoing["command"],
        'arguments': outgoing["arguments"]
      }),
    );

    if (!(response.statusCode == 200)) {
      throw Exception('Failed to Send "$command" to Robot World\'s Server.');
    }
  }

  /// Sends a "back 1" command request to the HTTP Server.
  Future sendBackCommand(String name) async {
    String command = "back \"1\"";
    Map outgoing = _processRequest(command, name);

    final response = await http.post(
      Uri.parse('http://${ip.text}:${port.text}/command'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'robot': outgoing["robot"],
        'command': outgoing["command"],
        'arguments': outgoing["arguments"]
      }),
    );

    if (!(response.statusCode == 200)) {
      throw Exception('Failed to Send "$command" to Robot World\'s Server.');
    }
  }

  /// Sends a "turn right" command request to the HTTP Server.
  Future sendTurnRightCommand(String name) async {
    String command = "turn \"right\"";
    Map outgoing = _processRequest(command, name);

    final response = await http.post(
      Uri.parse('http://${ip.text}:${port.text}/command'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'robot': outgoing["robot"],
        'command': outgoing["command"],
        'arguments': outgoing["arguments"]
      }),
    );

    if (!(response.statusCode == 200)) {
      throw Exception('Failed to Send "$command" to Robot World\'s Server.');
    }
  }

  /// Sends a "turn left" command request to the HTTP Server.
  Future sendTurnLeftCommand(String name) async {
    String command = "turn \"left\"";
    Map outgoing = _processRequest(command, name);

    final response = await http.post(
      Uri.parse('http://${ip.text}:${port.text}/command'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'robot': outgoing["robot"],
        'command': outgoing["command"],
        'arguments': outgoing["arguments"]
      }),
    );

    if (!(response.statusCode == 200)) {
      throw Exception('Failed to Send "$command" to Robot World\'s Server.');
    }
  }

  /// Process a request given by the user and returns a Map of the request.
  /// The format of the Map conforms to the HTTP Servers request requirements
  Map _processRequest(String request, String name) {
    // build json and send on output stream
    List commandSplit = request.trim().split(" ");
    List arguments = [];

    Map requestJSON = {"robot": name, "command": commandSplit[0]};

    if (commandSplit.length > 1) {
      arguments.add(commandSplit[1]);
      requestJSON["arguments"] = arguments.toString();
    } else {
      requestJSON["arguments"] = "[]";
    }

    return requestJSON;
  }
}
