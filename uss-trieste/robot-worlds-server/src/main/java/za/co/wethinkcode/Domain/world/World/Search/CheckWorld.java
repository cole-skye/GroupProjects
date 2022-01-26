package za.co.wethinkcode.Domain.world.World.Search;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.World.IWorld;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.util.*;

/**
 * Is used to check the current world for worldObjects within the robots visibility.
 */
public class CheckWorld {

    static int object_X;
    static int object_Y;

    static ArrayList<Map<String, Object>> objects = new ArrayList<>();

    /**
     * @param robot - Robot robot
     * @return objects - ArrayList<Map<String, Object>> object
     * Looks for worldObjects and robots within the given visibility of robot.
     */
    public static ArrayList<Map<String, Object>> doSearch(Robot robot) {

        // obstacle list
        List<WorldObject> obstacleList = SocketServer.getWorld().getObstacles();

        // robot list
        List<Robot> robotList = SocketServer.getWorld().getRobotList();

        // config visibility
        int visibility = SocketServer.configReader.getVisibility();

        // found World Objects
        ArrayList<Map<String, Object>> worldObjects = new ArrayList<>();

        // checks for any world objects within visibility
        for (int i = 1; i <= visibility; i++) {
            checkObstacles(robot, obstacleList, worldObjects, i);
            checkRobots(robot, robotList, worldObjects, i);
        }

        objects = worldObjects;

        return objects;
    }

    /**
     * @param robot - Robot robot
     * @param obstacleList - List<WorldObject> obstacleList
     * @param worldObjects - ArrayList<Map<String, Object>> worldObjects
     * @param currentLooksAhead - int currentLooksAhead
     * Checks each direction of the robot for obstacles and adds them to the worldObjects
     */
    private static void checkObstacles(
            Robot robot,
            List<WorldObject> obstacleList,
            ArrayList<Map<String, Object>> worldObjects,
            int currentLooksAhead)
    {
        for (WorldObject obstacle : obstacleList) {
            object_X = obstacle.getX();
            object_Y = obstacle.getY();

            if(objectInViewNorth(robot.getPosition().getY(), robot.getPosition().getX(), currentLooksAhead, 4)){
                worldObjects.add(createLookObjectInfo(
                        IWorld.Direction.NORTH,
                        "OBSTACLE",
                        currentLooksAhead));
            }
            else if(objectInViewSouth(robot.getPosition().getY(), robot.getPosition().getX(), currentLooksAhead, 4)){
                worldObjects.add(createLookObjectInfo(
                        IWorld.Direction.SOUTH,
                        "OBSTACLE",
                        currentLooksAhead));
            }
            else if(objectInViewToWest(robot.getPosition().getY(), robot.getPosition().getX(), currentLooksAhead, 4)){
                worldObjects.add(createLookObjectInfo(
                        IWorld.Direction.WEST,
                        "OBSTACLE",
                        currentLooksAhead));
            }
            else if(objectInViewToEast(robot.getPosition().getY(), robot.getPosition().getX(), currentLooksAhead, 4)){
                worldObjects.add(createLookObjectInfo(
                        IWorld.Direction.EAST,
                        "OBSTACLE",
                        currentLooksAhead));
            }
        }
    }

    /**
     * @param myRobot - Robot robot
     * @param robotList - List<WorldObject> robotList
     * @param worldObjects - ArrayList<Map<String, Object>> worldObjects
     * @param currentLooksAhead - int currentLooksAhead
     * Checks each direction of the robot for robots and adds them to the worldObjects.
     */
    private static void checkRobots(Robot myRobot,
                             List<Robot> robotList,
                             ArrayList<Map<String, Object>> worldObjects,
                             int currentLooksAhead)
    {
        for (Robot robot : robotList) {

            object_X = robot.getPosition().getX();
            object_Y = robot.getPosition().getY();

            // Checks if there are any other robots that do not match current robots name
            if(!myRobot.getName().equals(robot.getName())){
                if(objectInViewNorth(myRobot.getPosition().getY(), myRobot.getPosition().getX(), currentLooksAhead, 0)){
                    worldObjects.add(createLookObjectInfo(
                            IWorld.Direction.NORTH,
                            "ROBOT",
                            currentLooksAhead));
                }
                else if(objectInViewSouth(myRobot.getPosition().getY(), myRobot.getPosition().getX(), currentLooksAhead, 0)){
                    worldObjects.add(createLookObjectInfo(
                            IWorld.Direction.SOUTH,
                            "ROBOT",
                            currentLooksAhead));
                }
                else if(objectInViewToWest(myRobot.getPosition().getY(), myRobot.getPosition().getX(), currentLooksAhead, 0)){
                    worldObjects.add(createLookObjectInfo(
                            IWorld.Direction.WEST,
                            "ROBOT",
                            currentLooksAhead));
                }
                else if(objectInViewToEast(myRobot.getPosition().getY(), myRobot.getPosition().getX(), currentLooksAhead, 0)){
                    worldObjects.add(createLookObjectInfo(
                            IWorld.Direction.EAST,
                            "ROBOT",
                            currentLooksAhead));
                }
            }
        }
    }

    /**
     * @param currentY - int currentY
     * @param currentX - int currentX
     * @param VisionOffset - int VisionOffset - how far the robot can see.
     * @param ObjectOffset - int ObjectOffset - size of obstacle
     * @return withinObject_X && withinObject_Y
     * Checks if robot's position lands on objects in the north direction.
     */
    private static boolean objectInViewNorth(int currentY, int currentX, int VisionOffset, int ObjectOffset){
        boolean withinObject_X = currentX >= object_X && currentX <= object_X + ObjectOffset;
        boolean withinObject_Y = currentY + VisionOffset == object_Y;

        return withinObject_X && withinObject_Y;
    }

    /**
     * @param currentY - int currentY
     * @param currentX - int currentX
     * @param VisionOffset - int VisionOffset - how far the robot can see.
     * @param ObjectOffset - int ObjectOffset - size of obstacle
     * @return withinObject_X && withinObject_Y
     * Checks if robot's position lands on objects in the south direction.
     */
    private static boolean objectInViewSouth(int currentY, int currentX, int VisionOffset, int ObjectOffset){
        boolean withinObject_X = currentX >= object_X && currentX <= object_X - ObjectOffset;
        boolean withinObject_Y = currentY - VisionOffset == object_Y + ObjectOffset;

        return  withinObject_Y && withinObject_X;
    }

    /**
     * @param currentY - int currentY
     * @param currentX - int currentX
     * @param VisionOffset - int VisionOffset - how far the robot can see.
     * @param ObjectOffset - int ObjectOffset - size of obstacle
     * @return withinObject_X && withinObject_Y
     * Checks if robot's position lands on objects in the east direction.
     */
    private static boolean objectInViewToEast(int currentY, int currentX, int VisionOffset, int ObjectOffset){
        boolean withinObject_X = currentX + VisionOffset == object_X;
        boolean withinObject_Y = currentY >= object_Y && currentY <= object_Y + ObjectOffset;

        return withinObject_Y && withinObject_X;
    }

    /**
     * @param currentY - int currentY
     * @param currentX - int currentX
     * @param VisionOffset - int VisionOffset - how far the robot can see.
     * @param ObjectOffset - int ObjectOffset - size of obstacle
     * @return withinObject_X && withinObject_Y
     * Checks if robot's position lands on objects in the west direction.
     */
    private static boolean objectInViewToWest(int currentY, int currentX, int VisionOffset, int ObjectOffset){
        boolean withinObject_X = currentX - VisionOffset == object_X + ObjectOffset;
        boolean withinObject_Y = currentY >= object_Y && currentY <= object_Y + ObjectOffset;

        return withinObject_Y && withinObject_X;
    }


    /**
     * @param direction - IWorld.Direction direction
     * @param type - String type
     * @param distance - int distance
     * @return lookObjectInfo - formatted response for the client
     */
    private static HashMap<String, Object> createLookObjectInfo(IWorld.Direction direction, String type, int distance){
        HashMap<String, Object> lookObjectInfo = new HashMap<>();
        lookObjectInfo.put("direction", direction);
        lookObjectInfo.put("type", type);
        lookObjectInfo.put("distance", distance);

        return lookObjectInfo;
    }
}
