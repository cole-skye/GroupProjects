import 'package:flutter/cupertino.dart';

/// Holds all data that will be used by the view after being updated by the controller.
class Model extends ChangeNotifier {
  late List<String> _messages;

  late Future<String> _response;

  Map _data = {};

  get response => _response;

  get messages => _messages;

  get data => _data;

  set dataIs(value) {
    _data = value;
  }
}
