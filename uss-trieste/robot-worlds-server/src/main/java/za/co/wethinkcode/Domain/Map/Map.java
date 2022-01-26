package za.co.wethinkcode.Domain.Map;


//Test
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.util.List;

/**
 * Interface to represent a maze. A World will be loaded with a Maze, and will delegate the work to check if a path is blocked by certain obstacles etc to this maze instance.
 */
public interface Map {

    /**
     * @return the list of obstacles, or an empty list if no obstacles exist.
     */
    List<WorldObject> getObstacles();

    /**
     * @return the list of pits, or an empty list if no pits exist.
     */
    List<WorldObject> getPits();

    /**
     * Adds obstacles to obstacle list.
     */
    void addObstacle(WorldObject obstacle);

    /**
     * Adds pits to obstacle list.
     */
    void addPit(WorldObject pit);

    /**
     * Sets name of robot.
     */
    void setName(String worldName);

    /**
     * @return name of robot.
     */
    String getName();
}
