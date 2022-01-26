package za.co.wethinkcode.Communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import za.co.wethinkcode.Domain.Configuration.ConfigReader;
import za.co.wethinkcode.Domain.RobotCommands.Command;
import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.ServerCommands.PurgeCommand;
import za.co.wethinkcode.Domain.world.World.AbstractWorld;

import java.io.*;

/**
 * Handles all client requests and responses in JSON formatted responses.
 * Is used by both the Socket SocketServer and HTTP SocketServer
 *
 */
// Communication class
public class Communication implements Runnable {

    private static final ConfigReader configReader = new ConfigReader();
    public static final JSONParser parser = new JSONParser();

    static JSONObject requestJSON;
    private static String name;
    private static boolean error;
    static AbstractWorld world;
    public final BufferedReader dis;
    public final PrintStream dos;
    boolean isloggedin;
    static Robot bot;


    // constructor
    public Communication(PrintStream dataOut, BufferedReader dataIn, AbstractWorld setWorld) {
        this.dis = dataIn;
        this.dos = dataOut;
        this.isloggedin = true;
        world = setWorld;
        error = false;
    }

    public Robot getBot() {
        return bot;
    }

    public static void setClientRobotName(String args){
        name = args;
    }

    /**
     * sets error to true.
     */
    public static void isError(){
        error = true;
    }

    /**
     * sets the response message for the robot after a request has been received.
     * @return JSON response message
     */
    public static JSONObject response(Robot robot, AbstractWorld world, JSONObject msgSend) throws JsonProcessingException {
        JSONObject response = new JSONObject();
        JSONObject msg = new JSONObject();

        String name = msgSend.get("robot").toString();
        setClientRobotName(name);
        String requestCommand = msgSend.get("command").toString();
        String requestArguments = "[\"" + msgSend.get("arguments").toString() + "\"]";

        error = false;

        try {
            if (msgSend.get("robot").equals("")){
                msg.put("message", "Robot does not exist");
                response.put("data", msg);
                isError();

            }else {
                Command command = Command.create(requestCommand + " " + requestArguments);

                robot.handleCommand(command);

                if (robot.getMessage().contains("At the")){
                    msg.put("message", robot.getMessage());
                    response.put("data", msg);

                } else if (robot.getMessage().equals("Obstructed")){
                    msg.put("message", robot.getMessage());
                    response.put("data", msg);

                } else if (robot.getStatus().equals("DEAD")){
                    new PurgeCommand(name);
                }
            }
        }

        // incorrect command handle
        catch(IllegalArgumentException | IOException e){
            msg.put("message", e.getMessage());
            response.put("data", msg);
            isError();
        }

        // if error occurred
        if (error) {
            response.put("result", "ERROR");
        }

        else if (msgSend.get("command").equals("state")) {
            response.put("result", "OK");
            response.put("state", getState(robot));
        }

        else if(msgSend.get("command").equals("look")){
            JSONObject objects = new JSONObject();
            objects.put("objects", robot.getMessage());
            response.put("result", "OK");
            response.put("data", objects);
            response.put("state", getState(robot));

        }
        else if(msgSend.get("command").equals("launch")){
            response.put("result", "OK");
            response.put("data", getWorldSettings(world, robot));
            response.put("state", getState(robot));
        }
        else {
            response.put("result", "OK");
            response.put("data", getData(robot));
            response.put("state", getState(robot));
        }

        return response;
    }

    /**
     * Run method that is used in the Socket SocketServer.
     * Sends and receives messages to and from the Socket SocketServer.
     */
    @Override
    public void run() {

        String received;

        while (true) {
            try {

                // receive the string
                received = dis.readLine();
                JSONObject launchResponse = new JSONObject();
                JSONObject response = new JSONObject();

                requestJSON = (JSONObject) parser.parse(received);


                if (requestJSON.get("command").toString().equals("launch")) {
                    name = requestJSON.get("robot").toString();
                    launchResponse = robotLaunch(name, world);
                    response = (JSONObject) launchResponse.get("response");
                }

                if (response.isEmpty()) {
                    response = response(bot, world, requestJSON);
                }
                dos.println(response);

            } catch (IOException | ParseException | NullPointerException e) {
                world.clearRobotList();
                break;
            }
        }

        try {
            // closing resources
            dis.close();
            dos.close();

        } catch (IOException e) {
            System.err.println("Second IO Exception");
        }
    }

    /**
     * Calculates the amount of nodes available for a new robot
     * @param world - AbstractWorld world
     * @return - int total - total number spaces in the current world
     */
    public static Integer maxRobots(AbstractWorld world){
        int SIZE = world.getWorldSize();
        int CIRC = SIZE*2 + SIZE*2;
        int total = (SIZE*2+1) * (SIZE*2+1) - CIRC;

        return total;
    }

    /**
     * Returns true if the world is at maximum capacity
     * @param world - AbstractWorld world
     * @return - boolean full - whether the world can hold any more robots.
     */
    public static Boolean isWorldFull(AbstractWorld world){
        boolean full;
        full = world.getRobotList().size() > maxRobots(world);
        return full;
    }

    /**
     * Launch a robot into the world to the server
     * @param name - String name - name given to the robot
     * @param world - AbstractWorld world - world the robot will be launched into.
     * @return - JSONObject values - holds the created response to the client and newly created robot.
     * @throws IOException
     */
    public static JSONObject robotLaunch(String name, AbstractWorld world) throws IOException {
        JSONObject response = new JSONObject();
        JSONObject msg = new JSONObject();
        JSONObject values = new JSONObject();

        bot = new Robot(name, world);

        int index = world.getRobotList().size() + 1;
        bot.setId(index);

        if (!world.addRobotToRobotList(bot)) {
            response.put("result", "ERROR");
            msg.put("message", "There are too many of you in this world");
            response.put("data", msg);

        } else if (isWorldFull(world)){
            response.put("result", "ERROR");
            msg.put("message", "No more space in this world");
            response.put("data", msg);

        }else {
            System.out.println(" > Launched \"" + name + "\" into world.");
        }

        values.put("robot", bot);
        values.put("response", response);
        return values;
    }

    /**
     * Gets the state of the robot as a JSON string
     * @param robot - Robot robot
     * @return robotState - JSON String of robot's state
     */
    public static JSONObject getState(Robot robot) {
        JSONObject robotState = new JSONObject();
        JSONArray robotPosition = new JSONArray();
        robotPosition.add(robot.getPosition().getX());
        robotPosition.add(robot.getPosition().getY());
        robotState.put("position", robotPosition);
        robotState.put("shields", robot.getShields());
        robotState.put("direction", robot.getCurrentDirection().toString());
        robotState.put("shots", robot.getShots());
        robotState.put("status", robot.getStatus());

        return robotState;
    }

    /**
     * return the default settings for robots set by world
     * @return robotData - data string for robot
     */
    public static JSONObject getWorldSettings(AbstractWorld world, Robot robot){
        JSONObject robotData = new JSONObject();
        robotData.put("visibility", world.getVisibility());
        robotData.put("reload", world.getReloadTime());
        robotData.put("repair", world.getRepairTime());
        robotData.put("mine", world.getSetMineTime());
        robotData.put("shields", world.getMaxShield());
        robotData.put("position", robot.getPosition().asList());

        return robotData;
    }

    /**
     * Gets the data from the robot
     * @param robot - Robot robot
     * @return JSONObject data
     */
    public static JSONObject getData(Robot robot){
        JSONObject data = new JSONObject();

        data.put("position", robot.getPosition().asList());
        data.put("visibility", configReader.getVisibility());
        data.put("message", robot.getMessage());
        return data;
    }

}
