package za.co.wethinkcode.Domain.Robot;

import za.co.wethinkcode.Domain.Configuration.ConfigReader;
import za.co.wethinkcode.Domain.RobotCommands.Command;
import za.co.wethinkcode.Domain.world.World.AbstractWorld;
import za.co.wethinkcode.Domain.world.World.IWorld;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.io.IOException;
import java.util.List;

public class Robot {
    private final ConfigReader configReader = new ConfigReader();

    public static final Position CENTRE = new Position(0,0);

    private Position position;
    private Position topLeft;
    private Position bottomRight;
    private IWorld.Direction currentDirection;
    private IWorld.UpdateResponse updateResponse;
    private List<Robot> robotList;
    private List<WorldObject> obstacleList;
    private List<WorldObject> pitList;
    private List<WorldObject> mineList;
    private boolean alive;
    private String status;
    private String name;
    private String message;
    private AbstractWorld world;
    private int shields;
    private int shots;
    private int numberOfMines;
    private boolean checkX;
    private boolean checkY;
    private int id;

    public Robot(String name, AbstractWorld abstractWorld) {
        setTopLeft(new Position(-(abstractWorld.getWorldSize() ), abstractWorld.getWorldSize() ));
        setBottomRight(new Position(abstractWorld.getWorldSize(),-(abstractWorld.getWorldSize() )));
        setAlive(true);
        setName(name);
        setCurrentDirection(IWorld.Direction.NORTH);
        setPosition(CENTRE);
        setStatus("NORMAL");
        setMessage(" > Ready");
        setShieldStrength( configReader.getMaxShield() );
        setNumberOfMines(5);
        setWorld(abstractWorld);
        setRobotList(world.getRobotList());
        setObstaclesList(world.getObstacles());
        setPitList(world.getPits());
        setMineList(world.getMines());
        setShots(1);

    }


    /**
     * @return The remaining amount of mines the robot has.
     */
    public int getNumberOfMines() {
        return this.numberOfMines;
    }

    public int getShots() {
        return this.shots;
    }

    public Integer getId() {
        return id;
    }

    /**
     * @return gets the current shield strength.
     */
    public int getShields() {
        return this.shields;
    }

    public Position getPosition() {
        return this.position;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return this.status;
    }

    public IWorld.Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public void setWorld(AbstractWorld world){
        this.world = world;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setObstaclesList(List<WorldObject> obstacleList){
        this.obstacleList = obstacleList;
    }

    public void setShots(int shots){
        this.shots = shots;
    }

    public void setBottomRight(Position bottomRight) {
        this.bottomRight = bottomRight;
    }

    public void setTopLeft(Position topLeft) {
        this.topLeft = topLeft;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setMineList(List<WorldObject> mineList) {
        this.mineList = mineList;
    }

    public void setPitList(List<WorldObject> pitList) {
        this.pitList = pitList;
    }

    public void setRobotList(List<Robot> robotList) {
        this.robotList = robotList;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setCurrentDirection(IWorld.Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    /**
     * @param shieldStrength value to set the shield strength.
     */
    public void setShieldStrength(int shieldStrength){
        this.shields = shieldStrength;
    }

    public void setNumberOfMines(int numberOfMines){
        this.numberOfMines = numberOfMines;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Robot handles the command to have its attributes updated
     * @param command
     * @return executed command
     * @throws IOException
     */
    public boolean handleCommand(Command command) throws IOException {
        return command.execute(this);
    }

    /**
     * Changes the position of the robot if valid
     * @param nrSteps
     * @return true if valid position
     * @return false if invalid position
     */
    public boolean updatePosition(int nrSteps) {
        int newX = this.position.getX();
        int newY = this.position.getY();

        switch (this.currentDirection) {
            case NORTH:
                newY += nrSteps;
                break;
            case EAST:
                newX += nrSteps;
                break;
            case SOUTH:
                newY -= nrSteps;
                break;
            default:
                newX -= nrSteps;
                break;
        }

        Position newPosition = new Position(newX, newY);

        updateResponse = validatePosition(newPosition);

        if (updateResponse.equals(IWorld.UpdateResponse.FAILED_OBSTRUCTED)) {
            setMessage("Obstructed");
            return false;

        } else if (updateResponse.equals(IWorld.UpdateResponse.ENCOUNTERED_MINE)) {
            loseShieldToMine();

            if (this.shields < 0) {
                setStatus("DEAD");
                setMessage("You have died due to a mine.");
                killRobot();
                return false;

            } else if (this.shields == 0) {
                setMessage("Your shield has been broken");

            } else {
                setMessage("You've detonated a mine and have lost 3 shield layers");
            }

        } else if (updateResponse.equals(IWorld.UpdateResponse.ENCOUNTERED_ROBOT)) {
            setMessage("Obstructed");
            return false;

        } else if (updateResponse.equals(IWorld.UpdateResponse.ENCOUNTERED_PIT)) {
            this.alive = false;
            setStatus("DEAD");
            setMessage("Fell");
            return false;

        } else if (updateResponse.equals(IWorld.UpdateResponse.FAILED_OUTSIDE_WORLD)) {
            setMessage("At the "+getCurrentDirection().toString()+" edge");
            return false;

        } else if (updateResponse.equals(IWorld.UpdateResponse.SUCCESS)) {
            return true;
        }

        return true;
    }

    /**
     * Checks for WorldObjects in the path of the robot
     * @param newPosition
     * @return response of what object is in the path of the robot
     */
    public IWorld.UpdateResponse validatePosition(Position newPosition) {

        // Checks if robot encounters obstacles.
        for (WorldObject obstacle : obstacleList) {
            if (obstacle.encounterObjectPos(newPosition) || obstacle.encounterObjectInPath(this.position, newPosition)) {
                return IWorld.UpdateResponse.FAILED_OBSTRUCTED;
            }
        }

        // Checks if robot encounters pits.
        for (WorldObject pit : pitList) {
            if (pit.encounterObjectPos(newPosition) || pit.encounterObjectInPath(this.position, newPosition)) {
                this.alive = false;
                setPosition(new Position(pit.getX(), pit.getY()));
                return IWorld.UpdateResponse.ENCOUNTERED_PIT;
            }
        }

        // Checks if robot encounters mines.
        for (WorldObject mine : mineList) {
            if (mine.encounterObjectPos(newPosition) || mine.encounterObjectInPath(this.position, newPosition)) {
                System.out.println(" >> This mine was detonated [" + mine.getX() + ", " + mine.getY() + "].");
                removeMine(mine);
                setPosition(new Position(mine.getX(), mine.getY()));
                return IWorld.UpdateResponse.ENCOUNTERED_MINE;
            }
        }

        // Checks if robot encounters robots.
        if (robotList.size() > 1){
            for (Robot robot : robotList) {
                if (robot.blocksPosition(newPosition) || blocksPath(this.position, newPosition)) {
                    return IWorld.UpdateResponse.ENCOUNTERED_ROBOT;
                }
            }
        }

        if (isNewPositionAllowed(newPosition)) {
            setPosition(newPosition);
            return IWorld.UpdateResponse.SUCCESS;
        }
        return IWorld.UpdateResponse.FAILED_OUTSIDE_WORLD;
    }

    /**
     * checks if position is within the world constraints
     * @param position
     * @return true if robot is not out of bounds
     */
    public boolean isNewPositionAllowed(Position position) {
        return position.isIn(topLeft, bottomRight);
    }

    /**
     * returns a string value of the robot's attributes
     * @return String of attributes
     */
    @Override
    public String toString() {
        return "[" + this.position.getX() + "," + this.position.getY() + "] "
                + this.name + "> " + this.status;
    }

    /**
     * @return Gets the current life status of the robot.
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * kills robot.
     */
    public void killRobot(){
        this.alive = false;
    }

    /**
     * Takes one from the shield's strength.
     */
    private void loseShieldToBullet(){
        this.shields--;
    }

    /**
     * Takes three from the shield's strength.
     */
    private void loseShieldToMine(){
        this.shields -= 3;
    }

    /**
     * @param mine removes this mine from the mineList in the world.
     */
    public void removeMine(WorldObject mine) {
        world.removeMine(mine);
    }

    /**
     * @param mine adds this mine to the mineList in the world.
     */
    public void addMine(WorldObject mine) {
        world.addMine(mine);
    }


    /**
     * Removes one mine from the robot artillery.
     */
    public void usedAMine() {
        this.numberOfMines--;
    }

    /**
     * @param position robot's position
     * @return true if position is true.
     */
    public boolean blocksPosition(Position position) {

        if (robotList.size()>1){
            for (Robot robot: robotList) {
                if (getName().equals(robot.getName())) {
                    continue;
                }
                else if (!getName().equals(robot.getName())) {
                    checkX = (position.getX() == robot.getPosition().getX());
                    checkY = (position.getY() == robot.getPosition().getY());
                }

                if (checkX && checkY){
                    break;
                }
            }
        }

        return checkX && checkY;
    }

    /**
     * @param a robot's original position
     * @param b robot's new position
     * @return true if position is true.
     */
    public boolean blocksPath(Position a, Position b) {

        for (Robot robot : robotList){
            if (a.getY() == b.getY()) {
                for (int i = Math.min(a.getX(), b.getX()); i <= Math.max(a.getX(), b.getX()); i++) {
                    if (blocksPosition(new Position(i, b.getY()))) {
                        return true;
                    }
                }
            }
            else if (a.getX() == b.getX()) {
                for (int j = Math.min(a.getY(), b.getY()); j <= Math.max(a.getY(), b.getY()); j++) {
                    if (blocksPosition(new Position(b.getX(), j))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * launch Robot
     * @param name
     * @param abstractWorld
     * @return
     */
    public static Robot create(String name, AbstractWorld abstractWorld){
        return new Robot(name, abstractWorld);
    }

}
