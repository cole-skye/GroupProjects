import World.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGui extends JFrame implements ActionListener{
    private static JFrame frame;
    private JButton connectButton;
    private JPanel MainPanel;
    private JTextField portField;
    private JLabel portLabel;
    World world;

    public static void main(String[] args) {
        frame = new JFrame("ServerGui");
        frame.setContentPane(new ServerGui().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ServerGui() {
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                ServerDisplay.CreateDisplay();
                ServerController.run(world, Integer.parseInt(portField.getText()));
//                String obstacles = ServerController.printObstructions(world);
//                textArea1.setText(obstacles);
//
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
