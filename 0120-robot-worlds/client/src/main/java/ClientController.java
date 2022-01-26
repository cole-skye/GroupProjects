import Robot.*;
import Robot.Types.*;
import ThreadManager.ClientHandler;

import java.util.*;

public class ClientController {

    /**
     * Retrieves input from the user.
     *
     * @param prompt the message to display before accepting input.
     * @return the input received from the user.
     */
    private static String getInput(String prompt) {
        String input;
        Scanner scan;

        scan = new Scanner(System.in);
        System.out.print(prompt);
        input = (scan.hasNextLine()) ? scan.nextLine() : "";
        while (input.isBlank()) {
            System.out.print(prompt);
            input = (scan.hasNextLine()) ? scan.nextLine() : input;
        }
        return input;
    }

    /**
     * Retrieves the robot type, specified by the given type String. The IRobot object,
     * corresponding with the type, is returned.
     *
     * @param type the robot type to return.
     * @param name the name to assign to the IRobot object.
     * @return IRobot object : if the specified robot type exists.
     *         NULL          : if the specified robot type does not exist.
     */
    private static IRobot getRobotType(String type, String name) {
        type = type.toLowerCase().trim();
        if (type.equals("normal")) return new NormalRobot(name);
        if (type.equals("sniper")) return new SniperRobot(name);
        if (type.equals("bomber")) return new BomberRobot(name);
        return null;
    }

    /**
     * Retrieves the name to assign to the IRobot object, from the user.
     *
     * @return the name chosen by the user.
     */
    private static String getName() {
        String name;

        name = getInput("Enter robot name: ");
        return name;
    }

    /**
     * Retrieves the robot type from the user. If the specified type exists, the IRobot object,
     * corresponding with the type of robot, is returned.
     *
     * @param name the name to assign to the IRobot object.
     * @return the IRobot object corresponding with the robot type.
     */
    private static IRobot getTypeFromUser(String name) {
        int i;
        IRobot robot;

        i = 0;
        robot = null;
        while (robot == null) {
            if (i > 0) {
                System.out.print("INVALID: Choose a valid robot type from the list: normal; sniper");
            }
            robot = getRobotType(getInput("Enter robot type: "), name);
            i++;
        }
        return robot;
    }

    /**
     * Creates an IRobot object, with the name and type of robot chosen by the user.
     *
     * @return the IRobot object with the specified name and robot type.
     */
    private static IRobot setupRobot() {
        return getTypeFromUser(getName());
    }

    /**
     * Prints an ASCII robot figure and the robot introduction to the terminal.
     *
     * @param name the robot's name.
     */
     private static void printRobotASCII(String name) {
        String robotString = ("         __\n" +
                " _(\\    |@@|\n" +
                "(__/\\__ \\--/ __\n" +
                "   \\___|----|  |   __\n" +
                "       \\ }{ /\\ )_ / _\\\n" +
                "       /\\__/\\ \\__O (__\n" +
                "      (--/\\--)    \\__/\n" +
                "      _)(  )(_\n" +
                "     `---''---`\n");
        System.out.println(robotString);
        System.out.println("Hello, my name is "+name+"!");
    }

    /**
     * Prints an ASCII heading to the terminal.
     */
    private static void printHeading() {
         String heading;

         heading = "\u001B[33m\n" +
                 " (        )           )                       )   (    (    (      (     \n" +
                 " )\\ )  ( /(    (   ( /(   *   )   (  (     ( /(   )\\ ) )\\ ) )\\ )   )\\ )  \n" +
                 "(()/(  )\\()) ( )\\  )\\())` )  /(   )\\))(   ')\\()) (()/((()/((()/(  (()/(  \n" +
                 " /(_))((_)\\  )((_)((_)\\  ( )(_)) ((_)()\\ )((_)\\   /(_))/(_))/(_))  /(_)) \n" +
                 "(_))    ((_)((_)_   ((_)(_(_())  _(())\\_)() ((_) (_)) (_)) (_))_  (_))   \n" +
                 "| _ \\  / _ \\ | _ ) / _ \\|_   _|  \\ \\((_)/ // _ \\ | _ \\| |   |   \\ / __|  \n" +
                 "|   / | (_) || _ \\| (_) | | |     \\ \\/\\/ /| (_) ||   /| |__ | |) |\\__ \\  \n" +
                 "|_|_\\  \\___/ |___/ \\___/  |_|      \\_/\\_/  \\___/ |_|_\\|____||___/ |___/  \n" +
                 "                                                                         \u001B[0m";
        System.out.println(heading);
        System.out.println("\u001B[33m\n                                                          TEAM 21\n\n\u001B[0m");
    }

    /**
     * Main controller function that executes the program.
     */
    public static void run() {
        IRobot robot;

        printHeading();
        robot = setupRobot();
        printRobotASCII(robot.getName());
        new ClientHandler(robot).start();
    }

    public static void main(String[] args) {
        run();
    }

}
