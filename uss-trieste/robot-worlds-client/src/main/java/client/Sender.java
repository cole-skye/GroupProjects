package client;

import java.io.PrintStream;
import java.util.Scanner;

import static client.Client.sendRequest;

/**
 * Is a Thread-based class that on start, asks the user for a robot name
 * Launches a robot into the robot world held by the socket server.
 * Sends the user's requests towards the socket server.
 */
public class Sender implements Runnable{
    private static final Scanner scn = new Scanner(System.in);

    private String message = "";

    public Sender(PrintStream out){

    }

    @Override
    public void run() {
        System.out.print(" > Please enter robot name: ");
        String name = scn.nextLine();
        while (name.isBlank()){
            System.out.print(" > Please enter a valid name: ");
            name = scn.nextLine();
        }

        System.out.println(" > Your robot has received a name. You can launch your robot into the world using the 'launch' command");

        while (!message.equals("off")) {
            // read the message to deliver.
            message = scn.nextLine();
            sendRequest(message, name);
        }
    }
}
