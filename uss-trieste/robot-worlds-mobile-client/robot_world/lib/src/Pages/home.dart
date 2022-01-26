import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:robot_world/src/Pages/player_home.dart';
import 'package:robot_world/src/controller.dart';
import 'package:robot_world/src/Components/theme.dart';
import 'package:mvc_application/view.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required String title}) : super(key: key);

  @override
  _HomePage createState() => _HomePage();
}

class _HomePage extends StateMVC<MyHomePage> {
  _HomePage() : super(Controller()){
    con = controller as Controller;
  }

  late Controller con;

  /// Creates an appbar with the title of the App
  /// Creates 3 text boxes and positions them correctly.
  /// Then requests user input for the Robot name,
  /// IP Address and the Port Number.
  /// Lastly creates an "Enter" button to submit the input
  /// and positions it correctly.
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: const Text(
          "Robot World",
          style: CustomTheme.title,
        ),
      ),
      body: Center(
          child: Column(
            children: [
              const SizedBox(height: 20.0),
              TextFormField(
                controller: Controller.name,
                decoration: const InputDecoration(border: OutlineInputBorder(), labelText: "Robot Name"),
              ),
              const SizedBox(height: 20.0),
              TextFormField(
                controller: Controller.ip,
                decoration: const InputDecoration(border: OutlineInputBorder(), labelText: "IP Address"),
              ),
              const SizedBox(height: 20.0),
              TextFormField(
                controller: Controller.port,
                decoration: const InputDecoration(border: OutlineInputBorder(), labelText: "Port Number"),
              ),
              Container(
                margin: const EdgeInsets.all(50.0),
                width: 150.0,
                height: 50.0,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.push(context,
                        MaterialPageRoute(
                            builder:(context) => const PlayerHome(title: "Robot World")));
                  },
                  style: ElevatedButton.styleFrom(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30.0),
                    ),
                  ),
                  child: const Text('Enter'),
                ),
              )
            ],
          )),
    );
  }
}
