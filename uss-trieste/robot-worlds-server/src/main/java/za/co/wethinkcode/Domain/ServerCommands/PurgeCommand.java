package za.co.wethinkcode.Domain.ServerCommands;

import za.co.wethinkcode.Communication.Communication;
import za.co.wethinkcode.Socket.SocketServer;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


/**
 * Removes specified robot from the world.
 */
public class PurgeCommand {
    List<Communication> robotsRemoved = new ArrayList<>();

    public PurgeCommand(String argument) throws IOException {
        purge(argument);
    }

    public void purge(String argument) throws IOException {

        for (Communication client : SocketServer.getClientList()) {
            if (client.getBot().getName().equals(argument)) {

                robotsRemoved.add(client);

                SocketServer.getWorld().removeRobotFromRobotList(client.getBot().getName());
                try {
                    client.dos.close();
                    client.dis.close();
                } catch (SocketException e) {

                }
            }
        }
        if (robotsRemoved.isEmpty()){
            System.out.println(" > Player not found.");
        }
        else {
            System.out.println(" > "+ argument + " has been kicked.");
        }

    }

}
