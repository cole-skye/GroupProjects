import Config.fileUtils;
import ThreadManager.ClientHandler;
import ThreadManager.ServerHandler;
import World.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerController {

    private static void printObstructions(World world) {
        List<Obstacle> obstacles;
        List<Pitfall> pitfalls;

        obstacles = world.getObstacles();
        System.out.println("These are the obstacles:");
        for (Obstacle obstacle: obstacles) {
            System.out.println("- ("+obstacle.getBottomLeftX()+","+obstacle.getBottomLeftY()
                    +") to ("+obstacle.getTopRightX()+","+obstacle.getTopRightY()+")");
        }
        System.out.println();
        pitfalls = world.getPitfalls();
        System.out.println("These are the pitfalls:");
        for (Pitfall pitfall: pitfalls) {
            System.out.println("- ("+pitfall.getBottomLeftX()+","+pitfall.getBottomLeftY()
                    +") to ("+pitfall.getTopRightX()+","+pitfall.getTopRightY()+")");
        }
        System.out.println();
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

    public static void run(World world, int portNum) {
        ArrayList<ClientHandler> threadList;
        Socket socket;
        ClientHandler clientThread;

        threadList = new ArrayList<>();
        printHeading();
        printObstructions(world);
        new ServerHandler(world, threadList).start();
        try (ServerSocket serverSocket = new ServerSocket(portNum)) {
            while(true) {
                socket = serverSocket.accept();
                clientThread = new ClientHandler(socket, threadList, world);
                threadList.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong in the Server main");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int portNum = 5056;
        run(new World(), portNum);
    }

}
