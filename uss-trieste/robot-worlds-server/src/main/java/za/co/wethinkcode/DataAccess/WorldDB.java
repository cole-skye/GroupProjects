package za.co.wethinkcode.DataAccess;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.world.World.AbstractWorld;
import za.co.wethinkcode.Domain.world.World.World;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;
import za.co.wethinkcode.SQLDatabase.WorldData;
import za.co.wethinkcode.WebAPI.ApiServer;

import java.util.*;

public class WorldDB implements DataAccessor{
    private final HashMap worldObjects = new HashMap();

    private AbstractWorld activeWorld = ApiServer.world;

    private void setActiveWorld(String name){
        this.activeWorld = fillWorldsDB(name);
    }

    public AbstractWorld getActiveWorld() {
        return this.activeWorld;
    }


    /**
     * Get a single world by name and overwrite the current world's properties.
     * @param name the specified world
     * @return World Objects contained in the world
     */
    @Override
    public HashMap get(String name) {
        this.worldObjects.clear();
        if (name.equals(activeWorld.getWorldName())){
            this.worldObjects.put("obstacles", getActiveWorld().getObstacles());
            this.worldObjects.put("size", getActiveWorld().getWorldSize());
            this.worldObjects.put("robots", getRobots(this.activeWorld));

            return this.worldObjects;

        }
        this.activeWorld.reset();
        setActiveWorld(name);
        this.worldObjects.put("obstacles", getActiveWorld().getObstacles());
        this.worldObjects.put("size", getActiveWorld().getWorldSize());
        this.worldObjects.put("robots", getRobots(this.activeWorld));

        return this.worldObjects;
    }

    public HashMap getAllRobots() {
        this.worldObjects.clear();
        System.out.println(getActiveWorld().getWorldName());

        this.worldObjects.put("robots", getRobots(this.activeWorld));

        return this.worldObjects;
    }

    @Override
    public boolean saveWorld(AbstractWorld world) {
        List names = WorldData.read("ServerWorlds", "name");
        WorldData.putWorldDB(world);
        return true;
    }

    @Override
    public List<String> loadWorld(){
        return WorldData.read("ServerWorlds", "name");

    }

    @Override
    public HashMap all() {
        this.worldObjects.put("obstacles", this.activeWorld.getObstacles());
        this.worldObjects.put("size", this.activeWorld.getWorldSize());
        this.worldObjects.put("robots", this.activeWorld.getRobotList());

        return this.worldObjects;
    }

    /**
     * launch a single robot to the database.
     * @param robot the robot to launch
     * @return the newly added Robot with it's newly set id
     */
    @Override
    public Robot add(Robot robot) {
        int index = getActiveWorld().getRobotList().size() + 1;
        robot.setId(index);

        return robot;
    }

    private List<String> getRobots(AbstractWorld world){
        List<String> names = new ArrayList<>();

        for(Robot robot : world.getRobotList()){
            names.add(robot.getName());
        }

        return names;
    }

    /**
     * @param name name of specified world
     * @return new active world of the properties received from the SQL database
     */
    private AbstractWorld fillWorldsDB(String name){

        int size = WorldData.getWorldSizeFromDB(name);
        int id = WorldData.getWorldId(name);
        List<WorldObject> obstacles = WorldData.getObstaclesFromDB( name );

        this.activeWorld.setId(id);
        this.activeWorld.setWorldName(name);
        this.activeWorld.setWorldSize(size);
        this.activeWorld.setObstacles(obstacles);

        return this.activeWorld;
    }


}
