package Commands.RobotCommands;

import Commands.RobotCommand;
import JsonUtils.JsonEncode;
import Robot.Robot;
import Robot.Robot.Direction;
import Robot.Position;
import World.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class LookCommand extends RobotCommand {
    int northObstructSteps;
    int southObstructSteps;
    int westObstructSteps;
    int eastObstructSteps;

    public LookCommand() {
        super("look");
        northObstructSteps = 0;
        southObstructSteps = 0;
        westObstructSteps = 0;
        eastObstructSteps = 0;
    }

    @Override
    public JSONObject execute(Robot target, World world) {
        Dictionary<String, String> obstructions;

        obstructions = populateDictionary(world, target);
        return createLookResponse(obstructions, target);
    }

    private JSONObject populateData(JSONObject response, Dictionary<String, String> obstructions) {
        JSONArray objects;
        JSONObject object;
        JSONObject data;
        String[] directions;
        int[] steps;
        int i;
        String type;

        objects = new JSONArray();
        data = (JSONObject) response.get("data");
        directions = new String[]{"NORTH", "EAST", "SOUTH", "WEST"};
        steps = new int[]{northObstructSteps, eastObstructSteps, southObstructSteps, westObstructSteps};
        i = 0;
        for (String direction : directions) {
            object = new JSONObject();
            type = obstructions.get(direction.toLowerCase());
            if (type == null) {
                i++;
                continue;
            }
            object.put("direction", direction);
            object.put("type", obstructions.get(direction.toLowerCase()));
            object.put("distance", steps[i]);
            objects.add(object);
            i++;
        }
        data.put("objects", objects);
        return data;
    }

    private JSONObject createLookResponse(Dictionary<String, String> obstructions , Robot robot) {
        JsonEncode encoder;
        JSONObject response;
        JSONObject data;

        encoder = new JsonEncode(robot, "OK", "look");
        response = encoder.response;
        data = populateData(response, obstructions);
        return response;
    }

    private Dictionary<String, String> populateDictionary(World world, Robot target) {
        Dictionary<String, String> obstructions;

        obstructions = new Hashtable<>();
        obstructions = addNearestObstruction("north", world, target, obstructions);
        obstructions = addNearestObstruction("east", world, target, obstructions);
        obstructions = addNearestObstruction("south", world, target, obstructions);
        obstructions = addNearestObstruction("west", world, target, obstructions);
        return obstructions;
    }

    private int getRobotDistance(Position robotPos, Robot otherRobot, String direction) {
        Position otherRobotPos;
        int steps;

        otherRobotPos = otherRobot.getPosition();
        switch (direction) {
            case "north":
                return (otherRobotPos.getY() - robotPos.getY());
            case "south":
                return (robotPos.getY() - otherRobotPos.getY());
            case "west":
                return (robotPos.getX() - otherRobotPos.getX());
        }
        return (otherRobotPos.getX() - robotPos.getX());
    }

    private Robot getNearestVisibleRobot(String direction, World world, Robot robot) {
        Position robotPos;
        List<Robot> robots;
        int visibility;
        int nearest;
        int steps;
        Robot nearestRobot;

        robotPos = robot.getPosition();
        robots = world.getAllRobots();
        visibility = Config.fileUtils.getVisibility();
        nearest = 200;
        nearestRobot = null;
        for (Robot foundRobot : robots) {
            if (robot.equals(foundRobot)) continue;
            steps = getRobotDistance(robotPos, foundRobot, direction);
            if (steps > 0) {
                if ((visibility >= steps) && (steps < nearest)) {
                    nearest = steps;
                    nearestRobot = foundRobot;
                }
            }
        }
        return nearestRobot;
    }

    private int getStepsToEdge(Position robotPos, String direction) {
        Position edgeBottomRight;
        Position edgeTopLeft;

        edgeBottomRight = Config.fileUtils.getBoundaryBottomRight();
        edgeTopLeft = Config.fileUtils.getBoundaryTopLeft();
        switch (direction) {
            case "north":
                return (edgeTopLeft.getY() - robotPos.getY());
            case "south":
                return(robotPos.getY() - edgeBottomRight.getY());
            case "west":
                return (robotPos.getX() - edgeTopLeft.getX());
        }
        return (edgeBottomRight.getX() - robotPos.getX());
    }

    private int findSmallestValue(int a, int b, int c) {
        if (a <= b && a <= c) {
            return a;
        } else if (b <= c && b <= a) {
            return b;
        }
        return c;
    }

    private void setDirectionSteps(int steps, String direction) {
        switch (direction) {
            case "north":
                northObstructSteps = steps;
                break;
            case "south":
                southObstructSteps = steps;
                break;
            case "west":
                westObstructSteps = steps;
                break;
            case "east":
                eastObstructSteps = steps;
        }
    }

    private static Position getVisibilityFinalDest(Position robotPos, Direction direction, int visibility) {
        int x;
        int y;
        int newX;
        int newY;

        x = robotPos.getX();
        y = robotPos.getY();
        newX = x;
        newY = y;
        switch (direction) {
            case NORTH:
                newY = y + visibility;
                break;
            case WEST:
                newX = x - visibility;
                break;
            case SOUTH:
                newY = y - visibility;
                break;
            case EAST:
                newX = x + visibility;
                break;
        }
        return new Position(newX, newY);
    }

    private static Robot.Direction convertStringToDirection(String direction) {
        switch (direction.toLowerCase()) {
            case "north":
                return Robot.Direction.NORTH;
            case "south":
                return Robot.Direction.SOUTH;
            case "west":
                return Robot.Direction.WEST;
        }
        return Robot.Direction.EAST;
    }

    private boolean edgeIsVisible(Position robot, int visibility, String direction) {
        Position finalDest;
        Position edgeBottomRight;
        Position edgeTopLeft;

        edgeBottomRight = Config.fileUtils.getBoundaryBottomRight();
        edgeTopLeft = Config.fileUtils.getBoundaryTopLeft();
        finalDest = getVisibilityFinalDest(robot, convertStringToDirection(direction), visibility);
        switch (direction.toLowerCase()) {
            case "north":
                return (finalDest.getY() >= (edgeTopLeft.getY()-visibility));
            case "east":
                return (finalDest.getX() >= (edgeBottomRight.getX()-visibility));
            case "south":
                return (finalDest.getY() <= (edgeBottomRight.getY()+visibility));
        }
        return (finalDest.getX() <= (edgeTopLeft.getX()+visibility));
    }

    private Dictionary<String, String> addNearestObstruction(String direction, World world, Robot robot, Dictionary<String, String> obstructions) {
        Obstacle foundObstacle;
        Pitfall foundPitfall;
        Robot foundRobot;
        int obsSteps;
        int pitSteps;
        int robotSteps;
        int edgeSteps;
        int smallest;

        foundObstacle = Obstacle.getNearestVisibleObstacle(direction, world, robot);
        foundPitfall = Pitfall.getNearestVisiblePitfall(direction, world, robot);
        foundRobot = getNearestVisibleRobot(direction, world, robot);

        obsSteps = (foundObstacle != null) ? Obstacle.getSteps(robot.getPosition(), foundObstacle, direction) : 200;
        pitSteps = (foundPitfall != null) ? Pitfall.getSteps(robot.getPosition(), foundPitfall, direction) : 200;
        robotSteps = (foundRobot != null) ? getRobotDistance(robot.getPosition(), foundRobot, direction) : 200;
        edgeSteps = (edgeIsVisible(robot.getPosition(), Config.fileUtils.getVisibility(), direction)) ? getStepsToEdge(robot.getPosition(), direction) : 200;
        smallest = findSmallestValue(obsSteps, pitSteps, robotSteps);
        smallest = Math.min(edgeSteps, smallest);
        if (smallest == 200) return obstructions;
        if ((smallest == edgeSteps) && edgeSteps <= Config.fileUtils.getVisibility()) {
            setDirectionSteps(edgeSteps, direction);
            obstructions.put(direction, "EDGE");
        } else if (smallest == obsSteps) {
            setDirectionSteps(obsSteps, direction);
            obstructions.put(direction, "OBSTACLE");
        } else if (smallest == pitSteps) {
            setDirectionSteps(pitSteps, direction);
            obstructions.put(direction, "PITFALL");
        } else if (smallest == robotSteps) {
            setDirectionSteps(robotSteps, direction);
            obstructions.put(direction, "ROBOT");
        }
        return obstructions;
    }
}
