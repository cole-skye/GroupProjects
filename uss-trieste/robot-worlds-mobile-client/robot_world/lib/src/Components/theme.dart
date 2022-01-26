import 'package:flutter/material.dart';

class CustomTheme {
  /// List of colors which can be used throughout app.
  static List<Color> colors = [
    Colors.red,
    Colors.blue,
    Colors.green,
    Colors.pink,
    Colors.yellow
  ];

  /// Custom heading style
  static const TextStyle heading = TextStyle(
    fontWeight: FontWeight.w500,
    fontSize: 30,
    color: Colors.white,
  );

  /// Custom text style
  static const TextStyle text = TextStyle(
    fontSize: 20,
    color: Colors.white,
  );

  /// Custom title styling
  static const TextStyle title = TextStyle(
    fontSize: 35,
    color: Colors.white,
  );

  ///Default themed app components
  static final ThemeData _defaultTheme = ThemeData(
    primarySwatch: Colors.orange,
    canvasColor: Colors.white,
    cardColor: Colors.grey,
    iconTheme: const IconThemeData(
      color: Colors.grey,
    ),
  );

  ///Blue themed app components
  static final ThemeData _blueTheme = ThemeData(
    primarySwatch: Colors.blue,
    canvasColor: Colors.grey,
    cardColor: Colors.blue,
    iconTheme: const IconThemeData(
      color: Colors.grey,
    ),
  );

  ///Red themed app components
  static final ThemeData _redTheme = ThemeData(
    primarySwatch: Colors.red,
    canvasColor: Colors.grey,
    cardColor: Colors.red,
    iconTheme: const IconThemeData(
      color: Colors.red,
    ),
  );

  ///eFootball PES themed app components
  static final ThemeData _pesTheme = ThemeData(
    primarySwatch: Colors.amber,
    canvasColor: Colors.indigo,
    cardColor: Colors.amber,
    iconTheme: const IconThemeData(
      color: Colors.amber,
    ),
  );

  ///Dark themed app components
  static final ThemeData _darkTheme = ThemeData(
    primarySwatch: Colors.grey,
    canvasColor: Colors.black45,
    cardColor: Colors.grey,
    iconTheme: const IconThemeData(
      color: Colors.white,
    ),
  );

  get orange => _defaultTheme;

  get blue => _blueTheme;

  get red => _redTheme;

  get pes => _pesTheme;

  get dark => _darkTheme;
}
