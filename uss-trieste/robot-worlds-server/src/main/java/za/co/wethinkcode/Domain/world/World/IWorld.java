package za.co.wethinkcode.Domain.world.World;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.util.List;

/**
 * The blueprint for a world's properties.
 */
public interface IWorld {

    /**
     * Enum used to track direction
     */
    enum Direction {
        NORTH, EAST, SOUTH, WEST;

    }

    /**
     * Enum that indicates response for updatePosition request
     */
    enum UpdateResponse {
        SUCCESS, // position was updated successfully
        FAILED_OUTSIDE_WORLD, // robot will go outside world limits if allowed, so it failed to update the position
        FAILED_OBSTRUCTED, // robot obstructed by at least one obstacle, thus cannot proceed.
        ENCOUNTERED_PIT, // robot fell into pit and dies
        ENCOUNTERED_ROBOT, // robot obstructed by a robot
        ENCOUNTERED_MINE // robot gets hit by mine planted by other robot or itself.
    }

    /**
     * Get current world's id.
     */
    int getId();

    /**
     * Get current world's name.
     */
    String getWorldName();

    /**
     * Get current world's configured size.
     */
    int getWorldSize();

    /**
     * Get current obstacle list.
     */
    List<WorldObject> getObstacles();

    /**
     * Get current pit list.
     */
    List<WorldObject> getPits();

    /**
     * Get current robot list.
     */
    List<Robot> getRobotList();

    /**
     * Get current world's configured visibility.
     */
    int getVisibility();

    /**
     * Get current world's configured reload time.
     */
    int getReloadTime();

    /**
     * Get current world's configured repair time.
     */
    int getRepairTime();

    /**
     * Get current world's configured mine time.
     */
    int getSetMineTime();

    /**
     * Get current world's configured max shield strength.
     */
    int getMaxShield();

    /**
     * Get current mines list.
     */
    List<WorldObject> getMines();

    /**
     * @param newWorldSize - int newWorldSize
     * Sets the worlds size.
     */
    void setWorldSize(int newWorldSize);

    /**
     * @param worldName - String worldName
     * Sets the current world's name.
     */
    void setWorldName(String worldName);

    /**
     * @param id - int id
     * Sets the current world's id.
     */
    void setId(int id);

    /**
     * @param obstacles - List<WorldObject> obstacles
     * Sets the obstacle list.
     */
    void setObstacles(List<WorldObject> obstacles);

    /**
     * @param pits - List pits.
     * Sets the current world's pits to the provided pits.
     */
    void setPits(List<WorldObject> pits);

    /**
     * @param x - int x value of obstacle.
     * @param y - int y value of obstacle.
     * Manually adds obstacle to obstacle list.
     */
    void addObstacles(int x, int y);

    /**
     * @param mine - WorldObject mine
     * Adds a mine to mines list.
     */
    void addMine(WorldObject mine);

    /**
     * Resets all the world's properties.
     */
    void reset();

    /**
     * Clears the obstacle list.
     */
    void clearObstacles();

    /**
     * @param mine - WorldObject mine
     * removes a mine in the mines list.
     */
    void removeMine(WorldObject mine);

    /**
     * @param robot - String robot
     * Checks that a robot is not present in robotList via its name.
     * Returns true if the robot is found and returns false if not.
     */
    Boolean containsRobot(String robot);

    /**
     * @param robot - Robot robot
     * Adds a robot to robotList.
     * Returns true if the robot is successfully added and returns false if not.
     */
    Boolean addRobotToRobotList(Robot robot);

    /**
     * @param robotName - String robotName
     * Checks that a robot is present in robotList and removes it.
     */
    void removeRobotFromRobotList(String robotName);

    /**
     * Clears the current robotList.
     */
    void clearRobotList();

}
