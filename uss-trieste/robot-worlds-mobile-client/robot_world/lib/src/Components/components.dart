import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:robot_world/src/Components/canvas_painter.dart';
import 'package:robot_world/src/controller.dart';

class Components {
  // Sketches the current robot world
  // based on the properties received from the server.
  static Widget world(int size, List obstacles, List robots) {
    List<Offset> obstaclePoints = [];
    List<Offset> robotPoints = [];

    for (int i = 0; i < robots.length; i++) {
      var point = Offset(double.parse('${robots[i]["position"]["x"]}.${0}'),
          double.parse('${robots[i]["position"]["y"]}.${0}'));
      robotPoints.add(point);
    }

    for (int i = 0; i < obstacles.length; i++) {
      var point = Offset(double.parse('${obstacles[i]["x"]}.${0}'),
          double.parse('${obstacles[i]["y"]}.${0}'));
      obstaclePoints.add(point);
    }

    return SizedBox(
        height: double.parse("$size.${0}"),
        width: double.parse("$size.${0}") * 1.5,
        child: Center(
            child: Card(
                color: Colors.black12,
                child: Center(
                    child: CustomPaint(
                        painter: OpenPainter(
                            obstaclePoints, robotPoints))))));
  }

  /// Styling forward button and giving it functionality
  /// to be able to move forward
  static Widget forwardButton(Icon icon, Controller con) {
    return ConstrainedBox(
      constraints: const BoxConstraints.tightFor(width: 54, height: 50),
      child: ElevatedButton(
        child: icon,
        onPressed: () {
          con.sendBackCommand(Controller.name.text);
        },
        style: ElevatedButton.styleFrom(
          shape: const CircleBorder(),
        ),
      ),
    );
  }

  /// Styling back button and giving it functionality
  /// to be able to move backwards
  static Widget backButton(Icon icon, Controller con) {
    return ConstrainedBox(
      constraints: const BoxConstraints.tightFor(width: 54, height: 50),
      child: ElevatedButton(
        child: icon,
        onPressed: () {
          con.sendForwardCommand(Controller.name.text);
        },
        style: ElevatedButton.styleFrom(
          shape: const CircleBorder(),
        ),
      ),
    );
  }

  /// Styling turn right button and giving it functionality
  /// to be able to turn right
  static Widget rightButton(Icon icon, Controller con) {
    return ConstrainedBox(
      constraints: const BoxConstraints.tightFor(width: 54, height: 50),
      child: ElevatedButton(
        child: icon,
        onPressed: () {
          con.sendTurnLeftCommand(Controller.name.text);
        },
        style: ElevatedButton.styleFrom(
          shape: const CircleBorder(),
        ),
      ),
    );
  }

  /// Styling turn left button and giving it functionality
  /// to be able to turn left
  static Widget leftButton(Icon icon, Controller con) {
    return ConstrainedBox(
      constraints: const BoxConstraints.tightFor(width: 54, height: 50),
      child: ElevatedButton(
        child: icon,
        onPressed: () {
          con.sendTurnRightCommand(Controller.name.text);
        },
        style: ElevatedButton.styleFrom(
          shape: const CircleBorder(),
        ),
      ),
    );
  }
}
