import 'package:flutter/material.dart';
import 'admin_home.dart';
import 'package:robot_world/src/controller.dart';

class NavBar extends StatelessWidget {
  const NavBar({Key? key}) : super(key: key);

  /// A drawer that slides from the left of the screen
  /// displays a robot icon with the player name
  /// also giving you two options buttons
  /// "admin" and "settings"

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        children: [
          UserAccountsDrawerHeader(accountName: Text(Controller.name.text),
            accountEmail: const Text(''),
            currentAccountPicture: const CircleAvatar(
              child: ClipOval(
                child: Icon(Icons.smart_toy_outlined),
              ),
            ),
          ),
          ListTile(
            leading:const Icon(Icons.arrow_right),
            title: const Text("Admin"),
            onTap: () {
              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => const AdminHome()
                  )
              );
            },
          ),
          const Divider(),
          ListTile(
            leading:const Icon(Icons.arrow_right),
            title: const Text("Settings"),
            onTap: () {},
          ),
          const Divider(),
        ],
      ),
    );
  }
}
