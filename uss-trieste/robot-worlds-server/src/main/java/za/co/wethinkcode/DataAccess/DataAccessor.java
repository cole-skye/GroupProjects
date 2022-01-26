package za.co.wethinkcode.DataAccess;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.world.World.AbstractWorld;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.util.HashMap;
import java.util.List;

public interface DataAccessor {
    /**
     * Get a single world by name.
     * @param name the specified world
     * @return a World Objects contained in the world
     */
    HashMap<String, List<WorldObject>> get(String name);

    /**
     * Get all worlds in the database
     * @return A list of worlds
     */
    HashMap all();

    /**
     * Add a single world to the database.
     * @param robot the world to add
     * @return the newly added World
     */
    Robot add(Robot robot);

    /**
     * Get the list of robots from the database.
     * @return A HashMap of the list of robots
     */
    HashMap getAllRobots();

    boolean saveWorld(AbstractWorld world);

    List<String> loadWorld();
}
