package za.co.wethinkcode.Domain.Map;

import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractMap implements Map {
    private List<WorldObject> obstacles;
    private List<WorldObject> pitList;
    private String name;

    public AbstractMap() {
        this.obstacles = new ArrayList<>();
        this.pitList = new ArrayList<>();
        this.name = "Abstract Maze";
    }

    /**
     * @return the list of obstacles, or an empty list if no obstacles exist.
     */
    @Override
    public List<WorldObject> getObstacles() {
        return this.obstacles;
    }

    /**
     * @return the list of pits, or an empty list if no pits exist.
     */
    @Override
    public List<WorldObject> getPits() {
        return this.pitList;
    }

    /**
     * Adds obstacles to obstacle list.
     */
    public void addObstacles(List<WorldObject> obs) {
        this.obstacles = obs;
    }

    /**
     *  Adds obstacles to the map to be used by the world.
     */
    @Override
    public void addObstacle(WorldObject obstacle){
        obstacles.add(obstacle);
    }

    /**
     * Adds pits to pit list.
     */
    @Override
    public void addPit(WorldObject pit){
        pitList.add(pit);
    }

    /**
     * @return name of robot.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Sets name of robot.
     */
    public void setName(String name) {
        this.name = name;
    }
}
