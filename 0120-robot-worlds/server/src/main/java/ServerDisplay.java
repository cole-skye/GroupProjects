import World.Obstacle;
import World.Pitfall;
import World.World;

import javax.swing.*;
import java.util.List;

public class ServerDisplay {
    private JPanel panel1;
    private JPanel Main;
    private JTextArea textArea1;
    private JLabel label;
    World world;

    List<Obstacle> obstacles;
    List<Pitfall> pitfalls;

    public ServerDisplay() {
        String textArea = "...";
//        obstacles = world.getObstacles();
//
//        for (Obstacle obstacle: obstacles) {
//            textArea = textArea + "- ("+obstacle.getBottomLeftX()+","+obstacle.getBottomLeftY()
//                    +") to ("+obstacle.getTopRightX()+","+obstacle.getTopRightY()+")";
//        }
        label.setText("These are the obstacles:\n");
        textArea1.setText(textArea);
    }

    public static void CreateDisplay() {
        JFrame frame = new JFrame("ServerDisplay");
        frame.setContentPane(new ServerDisplay().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
