package za.co.wethinkcode.Domain.ServerCommands;

import za.co.wethinkcode.Socket.SocketServer;

/**
 * Ends server connection.
 */
public class QuitCommand {

    public QuitCommand(){
        SocketServer.Quit();
    }

}
