import 'package:mvc_application/view.dart';

import 'Components/theme.dart';
import 'Pages/home.dart';

class View extends AppState {
  factory View() => _this ??= View._();
  View._()
      : super(
          title: 'Uss-Trieste Robot Worlds',
          home: const MyHomePage(title: 'Robot World'),
          debugShowCheckedModeBanner: false,
          theme: CustomTheme().orange,
        );
  static View? _this;
}
