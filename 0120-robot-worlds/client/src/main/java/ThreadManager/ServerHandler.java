package ThreadManager;

import Commands.ShutdownCommand;
import Robot.IRobot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ServerHandler extends Thread {
    private IRobot robot;
    private Socket socket;
    private BufferedReader reader;
    private JSONObject commandResponse;

    public ServerHandler(Socket socket, BufferedReader reader, IRobot robot) {
        this.robot = robot;
        this.socket = socket;
        this.reader = reader;
        commandResponse = null;
    }

    public JSONObject getCommandResponse() {
        return this.commandResponse;
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

    private void processCommandResponse(JSONObject response) {
        commandResponse = response;
    }

    private void processOtherCommand(JSONObject response) {
        String status;
        String message;

        status = getStatus(response);
        message = "<"+robot.getPosition().getX()+","+robot.getPosition().getY()+"> "+robot.getName()+" : "+status;
        if (status.equals("DEAD")) {
            message = "<"+robot.getPosition().getX()+","+robot.getPosition().getY()+"> "+robot.getName()+" : I got killed! Bye bye...";
            ShutdownCommand.execute(message);
        }
        System.out.println(message);
    }

    private String getStatus(JSONObject response) {
        JSONObject state;
        String status;

        state = (JSONObject) response.get("state");
        status = (String) state.get("status");
        return status;
    }

    private String getMessage(JSONObject response) {
        JSONObject data;
        String message;

        data = (JSONObject) response.get("data");
        message = (String) data.get("message");
        return message;
    }

    private boolean isCommandResponse(JSONObject response) {
        String status;
        String message;

        try {
            status = getStatus(response);
            message = getMessage(response);
            if (status.equals("DEAD")) {
                //robot died - purge/dump/mine/attack
                return !((message.toLowerCase().contains("purge"))||
                         (message.toLowerCase().contains("dump"))||
                         (message.toLowerCase().contains("hit")));
            } else if (status.equals("NORMAL") || status.equals("RELOAD") || status.equals("SETMINE")) {
                //alive - no attack/attack survive
                return !((message.toLowerCase().contains("repair"))||
                         (message.toLowerCase().contains("hit"))||
                         (message.toLowerCase().contains("miss")));
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public void run() {
        String response;
        JSONObject jsonResponse;

        while (true) {
            try {
                while (true) {
                    response = reader.readLine();
                    if (!response.isBlank()) break;
                }
            } catch (IOException e) {
                System.out.println("Server logged off");
                break;
            }
            jsonResponse = stringToJson(response);
            if(jsonResponse != null) {
                if(isCommandResponse(jsonResponse)) {
                    processCommandResponse(jsonResponse);
                } else {
                    processOtherCommand(jsonResponse);
                }
            } else {
                System.out.println("A");
            }
            //todo create error
        }
        this.interrupt();
    }
}
