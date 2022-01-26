package za.co.wethinkcode.Domain.world.World;

import za.co.wethinkcode.Domain.Map.Map;
import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.WorldObjects.Obstacle.SquareObstacle;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements IWorld interface.
 * The class that controls the games world and its properties.
 */
public abstract class AbstractWorld implements IWorld {
    private final List<Robot> robotList = new ArrayList<>();
    private final List<WorldObject> mines = new ArrayList<>();

    private List<WorldObject> obstacles;
    private List<WorldObject> pitList;
    private String worldName;
    private int visibility;
    private int reloadTime;
    private int repairTime;
    private int setMineTime;
    private int maxShield;
    private int worldSize;
    private int id;

    public AbstractWorld(Map map){
        setWorldName(map.getName());
        setWorldSize(SocketServer.configReader.getWorldSizes());
        setObstacles(map.getObstacles());
        setPits(map.getPits());
        setVisibility(SocketServer.configReader.getVisibility());
        setReloadTime(SocketServer.configReader.getReloadTime());
        setRepairTime(SocketServer.configReader.getRepairTime());
        setMineTime(SocketServer.configReader.getSetMineTime());
        setMaxShield(SocketServer.configReader.getMaxShield());

    }

    /**
     * Resets all the world's properties.
     */
    @Override
    public void reset() {
        this.robotList.clear();
        this.pitList.clear();
        this.mines.clear();
        this.obstacles.clear();
    }

    /**
     * Clears the obstacle list.
     */
    @Override
    public void clearObstacles(){this.obstacles.clear();}

    /**
     * Get current obstacle list.
     */
    @Override
    public List<WorldObject> getObstacles() {
        return obstacles;
    }

    /**
     * Get current robot list.
     */
    @Override
    public List<Robot> getRobotList() {
        return robotList;
    }

    /**
     * Get current mines list.
     */
    @Override
    public List<WorldObject> getMines() {
        return mines;
    }

    /**
     * Get current pit list.
     */
    @Override
    public List<WorldObject> getPits() {
        return pitList;
    }

    /**
     * Get current world's configured visibility.
     */
    @Override
    public int getVisibility(){return this.visibility;}

    /**
     * Get current world's configured reload time.
     */
    @Override
    public int getReloadTime(){return this.reloadTime;}

    /**
     * Get current world's configured repair time.
     */
    @Override
    public int getRepairTime(){return this.repairTime;}

    /**
     * Get current world's configured mine time.
     */
    @Override
    public int getSetMineTime(){return this.setMineTime;}

    /**
     * Get current world's configured max shield strength.
     */
    @Override
    public int getMaxShield(){return this.maxShield;}

    /**
     * Get current world's name.
     */
    @Override
    public String getWorldName(){
        return this.worldName;
    }

    /**
     * Get current world's configured size.
     */
    @Override
    public int getWorldSize(){
        return this.worldSize;
    }

    /**
     * Get current world's id.
     */
    @Override
    public int getId(){
        return this.id;
    }

    /**
     * @param visibility - int visibility
     * Sets the world's configured visibility for the robot.
     */
    void setVisibility(int visibility){
        this.visibility = visibility;
    }

    /**
     * @param reloadTime - int reloadTime
     * Sets the world's configured reloadTime for the robot.
     */
    void setReloadTime(int reloadTime){
        this.reloadTime = reloadTime;
    }

    /**
     * @param repairTime - int repairTime
     * Sets the world's configured repairTime for the robot.
     */
    void setRepairTime(int repairTime){
        this.repairTime = repairTime;
    }

    /**
     * @param mineTime - int mineTime
     * Sets the world's configured mineTime for the robot.
     */
    void setMineTime(int mineTime){
        this.setMineTime = mineTime;
    }

    /**
     * @param maxShield - int maxShield
     * Sets the world's configured maxShield for the robot.
     */
    void setMaxShield(int maxShield){
        this.maxShield = maxShield;
    }

    /**
     * @param newWorldSize - int newWorldSize
     * Sets the worlds size.
     */
    @Override
    public void setWorldSize(int newWorldSize){
        worldSize = newWorldSize;
    }

    /**
     * @param obstacles - List<WorldObject> obstacles
     * Sets the obstacle list.
     */
    @Override
    public void setObstacles(List<WorldObject> obstacles) {
        this.obstacles = obstacles;
    }

    /**
     * @param pits - List pits.
     * Sets the current world's pits to the provided pits.
     */
    @Override
    public void setPits(List<WorldObject> pits) {
        this.pitList = pits;
    }

    /**
     * @param worldName - String worldName
     * Sets the current world's name.
     */
    @Override
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    /**
     * @param id - int id
     * Sets the current world's id.
     */
    @Override
    public void setId(int id){
        this.id = id;
    }

    /**
     * @param robot - String robot
     * Checks that a robot is not present in robotList via its name.
     * Returns true if the robot is found and returns false if not.
     */
    @Override
    public Boolean containsRobot(String robot) {
        for (Robot r : robotList) {
            if (r.getName().equalsIgnoreCase(robot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param x - int x value of obstacle.
     * @param y - int y value of obstacle.
     * Manually adds obstacle to obstacle list.
     */
    @Override
    public void addObstacles(int x, int y){
        obstacles.add(new SquareObstacle(x, y));
    }

    /**
     * @param mine - WorldObject mine
     * Adds a mine to mines list.
     */
    @Override
    public void addMine(WorldObject mine){
        mines.add(mine);
    }

    /**
     * @param robot - Robot robot
     * Adds a robot to robotList.
     * Returns true if the robot is successfully added and returns false if not.
     */
    @Override
    public Boolean addRobotToRobotList(Robot robot){
        if (containsRobot(robot.getName())){
            return false;
        } else {
            robotList.add(robot);
            return true;
        }
    }

    /**
     * @param mine - WorldObject mine
     * removes a mine in the mines list.
     */
    @Override
    public void removeMine(WorldObject mine){
        mines.remove(mine);
    }

    /**
     * Clears the current robotList.
     */
    @Override
    public void clearRobotList() {
        robotList.clear();
        System.out.println("* Clients disconnected: Removing all robots");
    }

    /**
     * @param robotName - String robotName
     * Checks that a robot is present in robotList and removes it.
     */
    @Override
    public void removeRobotFromRobotList(String robotName){
        robotList.removeIf(robot -> robot.getName().equals(robotName));
    }

}
