import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:robot_world/src/Components/theme.dart';

/// Is used to draw on the canvas of the mobile app.
/// Uses the list of robots and list of obstacles.
class OpenPainter extends CustomPainter {
  List<Offset> obstacles;
  List<Offset> robots;

  OpenPainter(this.obstacles, this.robots);

  /// Draws the obstacles based on the provided points.
  void paintObstacles(Canvas canvas, Size size) {
    var paint1 = Paint()
      ..color = Colors.white
      ..strokeWidth = 5;
    //draw points on canvas
    canvas.drawPoints(PointMode.points, obstacles, paint1);
  }

  /// Draws rounded points of the robots based on the provided points.
  void paintRobots(Canvas canvas, Size size) {
    var paint1 = Paint()
      ..color = CustomTheme.colors[3]
      ..strokeWidth = 5
      ..strokeCap = StrokeCap.round;

    //draw points on canvas
    canvas.drawPoints(PointMode.points, robots, paint1);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) => true;

  @override
  void paint(Canvas canvas, Size size) {
    paintObstacles(canvas, size);
    paintRobots(canvas, size);
  }
}
