import 'package:mvc_application/view.dart';
import 'package:provider/provider.dart';
import 'package:robot_world/src/model.dart';

import 'src/view.dart';

void main() {
  runApp(
      ChangeNotifierProvider(create: (context) => Model(), child: MyApp()));
}

// This widget is the root of your application.
class MyApp extends AppStatefulWidget {
  MyApp({Key? key}) : super(key: key);

  @override
  AppState createView() => View();
}
