import 'package:flutter/material.dart';
import 'package:robot_world/src/admin_buttons/delete_obstacle.dart';
import 'package:robot_world/src/admin_buttons/load_world.dart';
import 'package:robot_world/src/admin_buttons/post_obstacles.dart';
import 'package:robot_world/src/admin_buttons/remove_player.dart';
import 'package:robot_world/src/admin_buttons/save_world.dart';
import 'package:robot_world/src/admin_buttons/show_robots.dart';


class AdminHome extends StatelessWidget {

  const AdminHome({Key? key}) : super(key: key);

  /// A list of buttons that is a available in the admin tab
  /// each button has a divider between them so they aren't stacked
  /// on top of each other.
  /// When clicked on each button reroutes to its respected page
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Admin'),
      ),
      body: Center(
        child: Column(
          children: [
            ListTile(
              leading:const Icon(Icons.arrow_right),
              title: const Text("Robots"),
              subtitle: const Text('Displays Players in the world.'),
              onTap: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => ShowRobots()
                    ));
              },
            ),
            const Divider(),
            ListTile(
              leading:const Icon(Icons.arrow_right),
              title: const Text("Terminate Player"),
              subtitle: const Text("Removes Player from the game."),
              onTap: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const RemovePlayer()
                    ));
              },
            ),
            const Divider(),
            ListTile(
              leading:const Icon(Icons.arrow_right),
              title: const Text("Add Obstacles"),
              subtitle: const Text("Populate the world with obstacles."),
              onTap: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const AddObstacle()
                    ));
              },
            ),
            const Divider(),
            ListTile(
              leading:const Icon(Icons.arrow_right),
              title: const Text("Delete Obstacles"),
              subtitle: const Text('Delete the listed obstacles'),
              onTap: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const DeleteObstacle()
                    ));
              },
            ),
            const Divider(),
            ListTile(
              leading: const Icon(Icons.arrow_right),
              title: const Text("Save World"),
              subtitle: const Text('Save the world map'),
              onTap: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const SaveWorld()
                    ));
              },
            ),
            const Divider(),
            ListTile(
              leading:const Icon(Icons.arrow_right),
              title: const Text("Load World"),
              subtitle: const Text('Load the world map'),
              onTap: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const LoadWorld()
                    ));
              },
            ),
            const Divider(),
          ],
        ),
      ),
    );
  }
}
