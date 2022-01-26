package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Client class
* Java implementation for multithreaded robot worlds client
*/
public class Client
{
    private static final List<String> messages = new ArrayList<>();

    final static int ServerPort = 7000;

    private static Socket socket;
    private static PrintStream out;
    private static BufferedReader in;


    /**
     * @param ipAddress - String ipAddress
     * @param port - int port
     * Connects to a Socket Server using a valid port number and ip address.
     */

    public static void connect(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        } catch (IOException e) {
            //error connecting should just throw Runtime error and fail test
            throw new RuntimeException("Error connecting to Robot Worlds server.", e);
        }
    }

    /**
     * @return boolean - if the socket is connected.
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

    /**
     * Closes all input and output streams, also closes the socket itself to disconnect the client form any server.
     * Throws a RuntimeException if the disconnection failed.
     */
    public static void disconnect() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            //error connecting should just throw Runtime error and fail test
            throw new RuntimeException("Error disconnecting from Robot Worlds server.", e);
        }
    }

    /**
     * @param msg - String msg
     * @param name - String name
     * Processes the user's input into a valid JSON object that the Robot World's Socket Server can understand.
     */
    public static void sendRequest(String msg, String name){
        messages.add(msg);

        // build json and send on output stream
        JSONObject requestJSON = new JSONObject();
        String[] commandSplit = msg.trim().split(" ");

        requestJSON.put("robot", name);

        requestJSON.put("command", commandSplit[0]);

        if (commandSplit.length > 1) {
            requestJSON.put("arguments", commandSplit[1]);
        } else {
            requestJSON.put("arguments", "[]");
        }

        out.println(requestJSON.toJSONString());
    }

    public static void main(String[] args) {
        connect("localhost", ServerPort);

        Thread listener = new Thread(new Listener(in));
        Thread sender = new Thread(new Sender(out));

        sender.start();
        listener.start();

        while (true) {

            if (!listener.isAlive() || !sender.isAlive()){
                System.out.println("You've been disconnected from the server. To join again, Restart client.");
                System.exit(1);
            }
        }
    }
}
