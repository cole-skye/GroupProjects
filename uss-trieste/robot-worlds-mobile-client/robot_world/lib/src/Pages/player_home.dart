import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mvc_application/view.dart';
import 'package:robot_world/src/Components/components.dart';
import 'package:robot_world/src/Components/theme.dart';
import 'package:robot_world/src/Navigation/nav_bar.dart';

import '../controller.dart';

class PlayerHome extends StatefulWidget {
  const PlayerHome({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  _PlayerHomeState createState() => _PlayerHomeState();
}

class _PlayerHomeState extends StateMVC<PlayerHome> {
  _PlayerHomeState() : super(Controller()) {
    con = controller as Controller;
  }

  late Controller con;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: const NavBar(),
      appBar: AppBar(
        centerTitle: true,
        title: Text(widget.title, style: CustomTheme.title),
      ),
      body: _createBody(),
    );
  }

  /// Displaying world screen with robot and obstacles
  /// within the center of the screen.
  /// A button is styled and created to allow the user
  /// to launch the robot into the world.
  /// The movement buttons are then being called and
  /// positioned correctly on the screen.
  _createBody() {
    var snapshot = con.data;

    con.fetchWorldData(Controller.ip, Controller.port);

    if (snapshot.isEmpty) {
      return const Center(child: CircularProgressIndicator());
    }
    return Column(
      children: [
        const Spacer(),
        Stack(children: [
          Container(
            alignment: Alignment.center,
            child: Center(
              child: Components.world(
                  snapshot["size"], snapshot["obstacles"], snapshot["robots"]),
            ),
          ),
        ]),
        const Spacer(),
        Container(
            margin: const EdgeInsets.all(15.0),
            width: 100.0,
            height: 50.0,
            child: ElevatedButton(
                onPressed: () {
                  con.launch(Controller.name.text);
                },
                style: ElevatedButton.styleFrom(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                ),
                child: const Text(
                  'Launch',
                  style: CustomTheme.text,
                ))),
        const SizedBox(height: 35.0),
        Components.forwardButton(const Icon(Icons.arrow_upward), con),
        const SizedBox(height: 10.0),
        Container(
          alignment: FractionalOffset.center,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Components.leftButton(const Icon(Icons.arrow_back_outlined), con),
              const SizedBox(width: 60.0),
              Components.rightButton(
                  const Icon(Icons.arrow_forward_outlined), con),
            ],
          ),
        ),
        const SizedBox(height: 10.0),
        Components.backButton(const Icon(Icons.arrow_downward), con),
        const SizedBox(height: 20.0),
      ],
    );
  }
}
