package ThreadManager;

import Commands.ShutdownCommand;
import JsonUtils.*;
import Robot.IRobot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler extends Thread {
    private IRobot robot;

    public ClientHandler(IRobot robot) {
        this.robot = robot;
    }

    /**
     * Converts the given String into a JSONObject.
     *
     * @param response the String to convert into a JSONObject.
     * @return JSONObject : if the String can be converted into a JSONObject.
     *         NULL       : if the String cannot be converted into a JSONObject.
     */
    private static JSONObject stringToJson(String response) {
        JSONParser parser;

        try {
            parser = new JSONParser();
            return (JSONObject) parser.parse(response);
        } catch (Exception ParseException) {
            return null;
        }
    }

    /**
     * Retrieves a Response from the server program.
     *
     * @param reader the BufferedReader object used to communicate with the server program.
     * @return the Response in the form of a JSONObject.
     */
    private static JSONObject getResponse(BufferedReader reader) {
        String response;

        while (true) {
            try {
                response = reader.readLine();;
                break;
            } catch (Exception e) {
                continue;
            }
        }
        return stringToJson(response);
    }

    /**
     * Creates a String array of command arguments using the instruction given by the user.
     *
     * @param instruction command and command arguments given by the user.
     * @return a String array containing the command arguments.
     */
    private static String[] getArgs(String[] instruction) {
        String[] args;

        if (instruction.length < 2) return new String[]{};
        args = new String[]{instruction[1]};
        return args;
    }

    /**
     * Sends the server program a Request, in String form.
     *
     * @param request the JSONObject containing the command the server program needs to execute,
     *                and the information needed to execute the command.
     * @param writer  the PrintWriter object used to communicate with the server program.
     */
    private static void sendRequest(JSONObject request, PrintWriter writer) {
        while (true) {
            try {
                writer.println(request.toString());
                break;
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * Launches the robot into the server program's 'world'-simulation.
     *
     * @param robot   the IRobot object to launch into the world.
     * @param writer  the PrintWriter object used to communicate with the server program.
     * @param reader  the BufferedReader object used to retrieve the response from the server program.
     * @return true  : if the robot could join the server program's 'world'-simulation.
     *         false : if the robot could not join the server program's 'world'-simulation.
     */
    public static boolean launchRobot(IRobot robot, PrintWriter writer, BufferedReader reader) {
        JsonEncode encoder;
        JsonDecode decoder;
        JSONObject request;
        JSONObject response;

        encoder = new JsonEncode(robot, "launch", getArgs(new String[]{"launch"}));
        request = encoder.getRequest();
        sendRequest(request, writer);
        response = getResponse(reader);
        decoder = new JsonDecode(response, robot);
        return decoder.successful();
    }

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
     * Retrieves the command and command arguments from the user.
     *
     * @param robot the IRobot object that needs to execute the command.
     * @return a String consisting out of the chosen command and command arguments.
     */
    private static String getInstruction(IRobot robot) {
        String[] valid;
        List<String> validCommands;
        String instruction;
        String status;

        valid = new String[]{"launch","forward", "back", "right", "left", "off", "look", "mine"};
        validCommands = new ArrayList<>(Arrays.asList(valid));
        status = "What should I do next? ";
        instruction = getInput("<"+robot.getPosition().getX()+","+robot.getPosition().getY()+"> "+robot.getName()+" : "+status);
        while (!(validCommands.contains(instruction.split(" ")[0]))) {
            System.out.print("INVALID: Please choose a valid command:\n- launch\n- forward <steps>\n- back <steps>\n- right\n- left\n");
            instruction = getInput("<"+robot.getPosition().getX()+","+robot.getPosition().getY()+"> "+robot.getName()+" : "+status);
        }
        return instruction;
    }

    @Override
    public void run() {
        BufferedReader reader;
        PrintWriter writer;
        boolean shouldContinue;
        String[] instruction;
        JsonEncode encoder;
        JSONObject request;
        JSONObject response;
        ServerHandler serverHandler;
        JsonDecode decoder;
        int wait;

        try (Socket socket = new Socket("localhost", 5056)) {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            shouldContinue = true;
            if (!launchRobot(robot, writer, reader)) {
                ErrorMessages.printErrorLaunch();
                shouldContinue = false;
            }
            serverHandler = new ServerHandler(socket, reader, robot);
            serverHandler.start();
            while(shouldContinue) {
                wait = 20;
                instruction = getInstruction(robot).split(" ");
                if (instruction[0].equals("off")||instruction[0].equals("quit")) {
                    ShutdownCommand.execute("<"+robot.getPosition().getX()+","+robot.getPosition().getY()+"> "+robot.getName()+" : "+"Shutting down...");
                }
                if (instruction[0].toLowerCase().equals("mine")){
                    robot.setStatus("Mine is being set...");
                    System.out.println(robot.getStatus());
                    wait = 12500;
                }
                encoder = new JsonEncode(robot, instruction[0], getArgs(instruction));
                request = encoder.getRequest();
                sendRequest(request, writer);
                Thread.sleep(wait);
                response = serverHandler.getCommandResponse();
                decoder = new JsonDecode(response, robot);
                decoder.setRobotStatus(robot, instruction[0], getArgs(instruction));
                System.out.println(robot.getStatus());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
