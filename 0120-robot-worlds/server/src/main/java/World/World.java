package World;

import Commands.RobotCommand;
import Config.fileUtils;
import Robot.*;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private final Position TOP_LEFT;
    private final Position BOTTOM_RIGHT;
    private List<Pitfall> pitfalls;
    private List<Obstacle> obstacles;
    private List<Robot> robots;
    private String status;
    private List<Mine> mines;

    public World() {
        fileUtils.addJsonToFile();
        TOP_LEFT = fileUtils.getBoundaryTopLeft();
        BOTTOM_RIGHT = fileUtils.getBoundaryBottomRight();
        robots = new ArrayList<>();
        pitfalls = generatePitfalls();
        obstacles = generateObstacles();
        mines = new ArrayList<>();
    }

    public void resetRobotsList(){
        robots = new ArrayList<>();
    }

    /**
     * Generates random number within the given boundaries.
     * @return random number
     */
    private int generateRandom(int lowerBound, int upperBound) {
        Random rdm;
        int answer;
        int random;

        rdm = new Random();
        answer = 0;
        while (answer == 0) {
            random = rdm.nextInt(upperBound-lowerBound)+lowerBound;
            answer = (random <= upperBound && random >= lowerBound) ? random : 0;
        }
        return answer;
    }

    public void addMine(Mine mine) {
        mines.add(mine);
    }


    private List<Pitfall> generatePitfalls() {
        List<Pitfall> pitfalls;
        Random random;
        int numberOfPits;
        int noPitfalls;
        int x;
        int y;

        pitfalls = new ArrayList<>();
        random = new Random();
        numberOfPits = 5;
        noPitfalls = random.nextInt(numberOfPits);
        for (int i = 0; i < noPitfalls + 1; i++) {
            x = generateRandom(TOP_LEFT.getX()+5, BOTTOM_RIGHT.getX()-5);
            y = generateRandom(BOTTOM_RIGHT.getY()+5, TOP_LEFT.getY()-5);
            if (((x >= -4) && (x <= 4)) && ((y >= -4) && (y <= 4))) continue;
            pitfalls.add(new Pitfall(x, y));
        }
        return pitfalls;
    }

    private List<Obstacle> generateObstacles() {
        List<Obstacle> obstacles;
        Random random;
        int numberOfObstacles;
        int noObstacles;
        int x;
        int y;

        obstacles = new ArrayList<>();
        random = new Random();
        numberOfObstacles = 5;
        noObstacles= random.nextInt(numberOfObstacles);
        for (int i = 0; i < noObstacles + 1; i++) {
            x = generateRandom(TOP_LEFT.getX()+5, BOTTOM_RIGHT.getX()-5);
            y = generateRandom(BOTTOM_RIGHT.getY()+5, TOP_LEFT.getY()-5);
            if (((x >= -4) && (x <= 4)) && ((y >= -4) && (y <= 4))) continue;
            obstacles.add(new Obstacle(x, y));
        }
        return obstacles;
    }

    /**
     * Enum that indicates response for updatePosition request
     */
    public enum UpdateResponse {
        SUCCESS, //position was updated successfully
        FAILED_OUTSIDE_WORLD, //robot will go outside world limits if allowed, so it failed to update the position
        FAILED_OBSTRUCTED_PITFALL, //robot obstructed by at least one pitfall, thus cannot proceed and dies.
        FAILED_OBSTRUCTED_OBSTACLE, //robot obstructed by at least one obstacle, thus cannot proceed.
        FAILED_OBSTRUCTED_ROBOT, //robot obstructed by at least one other robot, thus cannot proceed.
        FAILED_OBSTRUCTED_MINE //robot stepped on mine
    }

    public JSONObject handleCommand(Robot target, RobotCommand command, World world) {
//        System.out.println("Start execution");
        return command.execute(target, world);
    }

    /**
     * Checks if the new position is obstructed by a pitfall, in the world.
     *
     * @param currentPosition Position object of the robot.
     * @param newPosition Position object after command performed.
     * @return true if the new position is obstructed.
     */
    public boolean obstructedByPit(Position currentPosition, Position newPosition) {
        for (Pitfall pitfall: pitfalls) {
            if (pitfall.fallIn(newPosition) || pitfall.inPitfall(currentPosition, newPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the new position is obstructed by a obstacle, in the world.
     *
     * @param currentPosition Position object of the robot.
     * @param newPosition Position object after command performed.
     * @return true if the new position is obstructed.
     */
    public boolean obstructedByObstacle(Position currentPosition, Position newPosition) {
        for (Obstacle obstacle: obstacles) {
            if (obstacle.blockPosition(newPosition) || obstacle.blockPath(currentPosition, newPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean obstructedMine(Position currentPosition, Position newPosition){
        for (Mine mine: mines){
            if (mine.mineInPosition(newPosition) || mine.mineInPath(currentPosition, newPosition)){
                return true;
            }
        }
        return false;
    }

    public boolean obstructedByRobot(Position newPosition, Robot robot) {
        Position checkPosition;

        for (Robot checkRobot: robots) {
            if (robot.getType().equals(checkRobot.getType())) continue;
            checkPosition = checkRobot.getPosition();
            if (robot.robotBlocksPosition(checkPosition) || (robot.robotBlocksPath(newPosition, checkPosition))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if position is within boundary of the world.
     *
     * @param position Position object to confirm if it is within the World object's boundary.
     * @return true if the position's x and y coordinates is within the boundary.
     */
    public boolean isNewPositionAllowed(Position position) {
        return position.isIn(TOP_LEFT,BOTTOM_RIGHT);
    }

    /**
     * Checks which UpdateResponse eNum should be returned, based off of
     * the given Position object.
     *
     * @param newPosition the Robot object's new position.
     * @param target      the Robot object in question.
     * @return the UpdateResponse eNum SUCCESS if the new position is allowed
     */
    public UpdateResponse getUpdateResponse(Position newPosition, Robot target) {
        if (obstructedByPit(target.getPosition(), newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED_PITFALL;
        } else if (obstructedByObstacle(target.getPosition(), newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED_OBSTACLE;
        } else if (obstructedByRobot(newPosition, target)) {
            return UpdateResponse.FAILED_OBSTRUCTED_ROBOT;
        } else if (obstructedMine(target.getPosition(), newPosition)) {
            return UpdateResponse.FAILED_OBSTRUCTED_MINE;
        } else if (isNewPositionAllowed(newPosition)) {
            target.setPosition(newPosition);
            return UpdateResponse.SUCCESS;
        }
        return UpdateResponse.FAILED_OUTSIDE_WORLD;
    }

    /**
     * Updates the robot's current position if the new position is a valid position to move to.
     *
     * @param target  the Robot object in question.
     * @param nrSteps the total amount of steps the IRobot object should move vertically OR horizontally.
     * @return UpdateResponse eNum SUCCESS if the robot's position was successfully updated.
     */
    public UpdateResponse updatePosition(Robot target, int nrSteps) {
        Robot.Direction currentDirection;
        Position currentPosition;
        Position newPosition;
        int newX;
        int newY;

        currentPosition = target.getPosition();
        currentDirection = target.getDirection();
        newX = currentPosition.getX();
        newY = currentPosition.getY();
        switch (currentDirection) {
            case NORTH:
                newY = newY + nrSteps;
                break;
            case EAST:
                newX = newX + nrSteps;
                break;
            case SOUTH:
                newY = newY - nrSteps;
                break;
            case WEST:
                newX = newX - nrSteps;
                break;
        }
        newPosition = new Position(newX, newY);
        return getUpdateResponse(newPosition, target);
    }

    public void addPitfallToList(Pitfall pitfall) {
        pitfalls.add(pitfall);
    }

    /**
     * Checks whether their is another robot, with the same Robot type variable as the given Robot object, in the
     * World object's list of Robot objects.
     *
     * @param target the robot object to compare to.
     * @return true if the Robot list contains an IRobot with the same Robot Type.
     */
    public boolean isRobotTypeInWorld(Robot target) {
        for (Robot robot : robots) {
            if (target.getType().equals(robot.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the robot object with the given name value, if this robot is
     * in the world.
     *
     * @param name the robot object's name.
     * @return the robot object with the given name.
     */
    public Robot getRobotWithName(String name) {
        for (Robot robot : robots) {
            if (robot.getName().equals(name)) {
                return robot;
            }
        }
        return null;
    }

    /**
     * Adds the given Robot object to the World object's list of
     * Robots. Only if the Robot object's Robot Type does not already exist
     * in the Robot list.
     *
     * @param target the Robot object to add to the list.
     * @return true if the robot was added to the Robot list.
     */
    public boolean addRobot(Robot target) {
        if (isRobotTypeInWorld(target)) {
            return false;
        }
        robots.add(target);
        return true;
    }

    /**
     * Removes the given Robot object from the World object's list of
     * Robots. Only if the Robot object already exist in the Robot list.
     *
     * @param target the Robot object to remove from the list.
     * @return true  : if the robot was removed from the Robot list.
     *         false : if the robot was not found in the Robot list, and
     *                 thus could not be removed.
     */
    public boolean removeRobot(Robot target) {
        try {
            robots.remove(target);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Retrieves the Robot object, with the same Robot Type as the given Robot object,
     * from the World object's list of Robots.
     *
     * @param target Robot object used to compare Robot Types with.
     * @return Robot object  : if an IRobot object, with identical Robot Class, was found.
     *         null          : if no IRobot object, with identical Robot Class, was found.
     */
    public Robot getRobot(Robot target) {
        for (Robot robot : robots) {
            if (robot.getClass().equals(target.getClass())) {
                return robot;
            }
        }
        return null;
    }

    public List<Pitfall> getPitfalls() {
        return pitfalls;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setPitfalls(List<Pitfall> pitfalls) {
        this.pitfalls = pitfalls;
    }

    public List<Robot> getAllRobots() {
        return this.robots;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    public void addObstacleToList(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

}
