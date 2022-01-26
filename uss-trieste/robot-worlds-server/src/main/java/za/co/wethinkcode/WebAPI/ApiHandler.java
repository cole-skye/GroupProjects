package za.co.wethinkcode.WebAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import za.co.wethinkcode.Communication.Communication;
import za.co.wethinkcode.DataAccess.DataAccessor;
import za.co.wethinkcode.DataAccess.WorldDB;
import za.co.wethinkcode.Domain.Robot.Robot;


import io.javalin.http.HttpCode;
import io.javalin.http.NotFoundResponse;
import za.co.wethinkcode.Domain.RobotCommands.Command;
import za.co.wethinkcode.Domain.RobotCommands.ShutdownCommand;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class ApiHandler {
    private static final DataAccessor database = new WorldDB();
    private static JSONObject reqJSON = new JSONObject();
    private static JSONObject response = new JSONObject();

    private static Robot robot;
    /**
     * Get all world properties
     *
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getAll(Context context) {
        context.json(database.all());

    }

    /**
     * Get one world
     * @param context The Javalin Context for the HTTP GET Request
     */
    public static void getOne(Context context) {
        String name = context.pathParamAsClass("name", String.class).get();
        HashMap<String, List<WorldObject>> worldObjects = database.get(name);
        if (worldObjects == null) {
            throw new NotFoundResponse("World not found: " + name);
        }
        context.json(worldObjects);

    }

    /**
     * Process command sent from client via the Domain's and Communications command handling process
     * Responds with a JSON response
     *
     * @param context The Javalin Context for the HTTP POST Request
     */
    public static void doCommand(Context context) {
        JSONObject reqJSON = new JSONObject();
        JSONObject response = new JSONObject();

        JSONObject request = context.bodyAsClass(JSONObject.class);
        try{
            reqJSON = (JSONObject) Communication.parser.parse(request.toString());

        } catch (NullPointerException | ParseException e){
            System.err.println("Could not parse request: \"" + request + "\"");
        }

        try{
            response = Communication.response(robot, ApiServer.world, reqJSON);

        }catch (JsonProcessingException e){
            System.err.println("Could not process command: " + e);
        }

        context.json(response);

    }

    /**
     * Create a new robot and executes a launch command.
     * Adds the new robot to the list of robots in the currently used world.
     *
     * @param context The Javalin Context for the HTTP POST Request
     */
    public static void create(Context context) {
        String name = context.pathParamAsClass("name", String.class).get();

        JSONObject request = new JSONObject();

        request.put("robot", name);
        request.put("command", "launch");
        request.put("arguments", "[]");

        if (request.get("command").toString().equals("launch")){
            try {
                response = Communication.robotLaunch(name, ApiServer.world);
                robot = (Robot) response.get("robot");

                if (response.isEmpty()) {
                    response = Communication.response(robot, ApiServer.world, request);
                }

            }catch (IOException e){
                System.err.println("Could not launch robot: " + name);
            }

        }

        Robot newRobot = database.add(robot);
        context.header("Location", "/robot" + newRobot.getId());
        context.status(HttpCode.CREATED);
        context.json(response);
    }

    /**
     * returns the list of robots from the world
     * @param context
     */
    public static void getAllRobots(Context context) {
        HashMap<String, List<Robot>> worldObjects = new HashMap<>();
        worldObjects.put("robots", ApiServer.world.getRobotList());
        context.json(worldObjects);
    }

    /**
     * gets name from the context to find and delete a specified robot
     * @param context
     */
    public static void deleteRobot(Context context) {
        String name = context.pathParamAsClass("name", String.class).get();
        for (Robot r : ApiServer.world.getRobotList()){
            if (r.getName().equals(name)){
                robot = r;
                break;
            }
        }
        ApiServer.world.removeRobotFromRobotList(name);
        Command offCommand = new ShutdownCommand();
        try {
            robot.handleCommand(offCommand);
        } catch (IOException e) {

        }

        context.json("Done");
    }

    /**
     * Adds a single obstacle to the world
     * @param context
     */
    public static void addObstacles(Context context) {
        JSONObject request = context.bodyAsClass(JSONObject.class);
        int x = Integer.parseInt(request.get("x").toString());
        int y = Integer.parseInt(request.get("y").toString());
        ApiServer.world.addObstacles(x, y);
        context.status(HttpCode.CREATED);
        context.json(ApiServer.world.getObstacles().toString());
    }

//    /**
//     * Saves the world into a database
//     * @param context
//     */
//    public static void saveWorld(Context context) {
//        boolean saved = database.saveWorld(ApiServer.world);
//        if (saved) {
//            context.status(HttpCode.CREATED);
//            context.json("Saved Successfully");
//        }else {
//            context.status(HttpCode.BAD_REQUEST);
//            context.json("Unsuccessful save");
//        }
////        database.saveWorld(ApiServer.world);
//    }

    public static void loadWorld(Context context){
        List names = database.loadWorld();
        context.json(names);
    }

    /**
     * clears the list of obstacles
     * @param context
     */
    public static void deleteObstacles(Context context) {
        ApiServer.world.clearObstacles();
        context.json("Deleted obstacles");
    }
}
