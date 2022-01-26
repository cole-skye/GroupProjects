package ThreadManager;

import Commands.RobotCommand;
import JsonUtils.JsonDecode;
import JsonUtils.JsonEncode;
import Robot.Robot;
import World.World;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    public static ArrayList<ClientHandler> clients;
    private World world;
    private PrintWriter writer;
    private Robot robot;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients, World world) {
        this.socket = socket;
        this.clients = clients;
        this.world = world;
        this.writer = generatePrintWriter();
    }

    private JSONObject stringToJson(String request) {
        JSONParser parser;

        try {
            parser = new JSONParser();
            return (JSONObject) parser.parse(request);
        } catch (Exception ParseException) {
            return null;
        }
    }

    private JSONObject getRequest(BufferedReader reader) {
        String request;

        try {
            request = reader.readLine();
            return stringToJson(request);
        } catch (Exception e) {
            return null;
        }
    }

    private JSONObject getResponse(JSONObject request, World world) {
        Robot robot;
        JsonDecode decoder;
        JsonEncode encoder;
        JSONObject response;
        RobotCommand command;

        if (!(request==null)) {
            decoder = new JsonDecode(world, request);
            robot = decoder.getRobot();
            if( robot == null && !(decoder.getCommand().equals("launch")) ) {
                robot = new Robot(decoder.getName(request), "normal", 0, 0);
                encoder = new JsonEncode(robot, "ERROR", decoder.getCommand() + " " + decoder.getArguments());
                response = encoder.response;
            } else {
                if(decoder.getCommand().equals("launch") ) {
                    String[] args = decoder.getArguments().split(" ");
                    robot = new Robot(decoder.getName(request), args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                    setRobot(robot);
                }
                command = RobotCommand.create(decoder.getCommand(), decoder.getArguments());
                response = world.handleCommand(robot, command, world);
            }
        } else {
            response = null;
        }
        return response;
    }

    public void sendToClient(JSONObject response) {
        writer.println(response.toString());
    }

    private PrintWriter generatePrintWriter() {
        PrintWriter writer;

        writer = null;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Robot getRobot() {
        return this.robot;
    }

    @Override
    public void run() {
        BufferedReader reader;
        JSONObject request;
        JSONObject response;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                request = getRequest(reader);
                if (request != null && request.get("command").equals("off")) {
                    System.out.println("Shutting down");
                    break;
                }
                response = getResponse(request, world);
                writer.println(response.toString());
            }
            socket.close();
            this.interrupt();
        } catch (NullPointerException | IOException e) {
            System.out.println(this.robot.getName()+" exited the world.");
            clients.remove(this);
            world.removeRobot(this.robot);
            this.interrupt();
        }
    }
}
