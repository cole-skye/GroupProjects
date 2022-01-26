package Robot;

import Commands.RobotCommands.FireCommand;

import java.util.ArrayList;
import java.util.Random;

public class Robot {

    private String name;
    private Position position;
    private Robot.Status status;
    private Robot.Direction direction;
    private String type;
    private int shieldStrength;
    private int shotsLeft;
    private int shotDistance;


    public Robot(String name, String type, int shieldStrength, int shotsLeft) {
        this.name = name;
        this.status = Status.NORMAL;
        this.direction = Direction.NORTH;
        this.type = type;
        this.shieldStrength = shieldStrength;
        this.shotsLeft = shotsLeft;
        this.shotDistance = FireCommand.getMaxShotDistance();
    }





    /**
     * Enum used to track direction
     */
    public enum Direction {
        NORTH, SOUTH, WEST, EAST
    }

    /**
     * Enum used to track the robot's status
     *
     * RELOADING - the robot is reloading weapons
     * REPAIRING - the robot is repairing shields
     * SETMINE - the robot is setting a mine
     * NORMAL - the robot is waiting for the next command
     * DEAD - the robot is dead and is no longer in the world.
     */
    public enum Status {
        RELOADING, REPAIRING, SETMINE, NORMAL, DEAD
    }

    public Robot.Direction getDirection() {
        return direction;
    }

    public void updateDirection(boolean turnRight){
        if (turnRight) {
            if (this.direction.equals(direction.NORTH)){
                this.direction = direction.EAST;
            } else if (this.direction.equals(direction.EAST)){
                this.direction = direction.SOUTH;
            } else if (this.direction.equals(direction.SOUTH)){
                this.direction = direction.WEST;
            } else if (this.direction.equals(direction.WEST)){
                this.direction = Direction.NORTH;
            }
        }
        else {
            if (this.direction.equals(direction.NORTH)){
                this.direction = direction.WEST;
            } else if (this.direction.equals(direction.WEST)){
                this.direction = direction.SOUTH;
            } else if (this.direction.equals(direction.SOUTH)){
                this.direction = direction.EAST;
            } else if (this.direction.equals(direction.EAST)){
                this.direction = direction.NORTH;
            }
        }
    }
    /**
     * Retrieves the Robot object's name.
     *
     * @return      the name of the Robot object.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the Robot object's Position object.
     *
     * @return      the Position object of the Robot object.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Retrieves the Robot object's status.
     *
     * @return      the status of the Robot object.
     */
    public Robot.Status getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    /**
     * Updates the Robot's current Position object, with the new Position object.
     *
     * @param  position    the new Position object to assign to the Robot object.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Retrieves the Robot object's shield strength.
     *
     * @return      the shield strength.
     */
    public int getShieldStrength() {
        return shieldStrength;
    }

    /**
     * Retrieves the Robot object's gun distance.
     *
     * @return      the gun distance.
     */
    public int getShotsLeft() {
        return shotsLeft;
    }

    /**
     * Updates the Robot's current status, with the given Status eNum.
     *
     * @param  s    the new status to assign to the Robot object.
     */
    public void setStatus(Status s) {
        status = s;
    }

    /**
     * Updates the Robot object's type, with the given type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Updates the Robot object's shieldStrength, with the given shieldStrength.
     */
    public void setShieldStrength(int shieldStrength) {
        this.shieldStrength = shieldStrength;
    }

    public boolean robotBlocksPosition(Position a) {
        int x;
        int y;
        int checkX;
        int checkY;

        x = position.getX();
        y = position.getY();
        checkX = a.getX();
        checkY = a.getY();
        return ((x == checkX) && (y == checkY));
    }

    public boolean robotBlocksPath(Position newPosition, Position checkPosition) {
        Position currentPosition;
        int startY;
        int endY;
        int startX;
        int endX;
        int checkX;
        int checkY;

        currentPosition = getPosition();
        checkY = checkPosition.getY();
        checkX = checkPosition.getX();
        if ((currentPosition.getX() == newPosition.getX()) && (currentPosition.getX() == checkX)) {
            startY = Math.min(currentPosition.getY(), newPosition.getY());
            endY = Math.max(currentPosition.getY(), newPosition.getY());
            for (int i = startY; i <= endY; i++) {
                if (i == checkY) return true;
            }
        } else if (currentPosition.getY() == checkY) {
            startX = Math.min(currentPosition.getX(), newPosition.getX());
            endX = Math.max(currentPosition.getX(), newPosition.getX());
            for (int j = startX; j <= endX; j++) {
                if (j == checkX) return true;
            }
        }
        return false;
    }

    public int getShotDistance() {
        return this.shotDistance;
    }

    public void setShotDistance(int shotDistance) {
        this.shotDistance = shotDistance;
    }

    public void setShotsLeft(int shotsLeft) {
        this.shotsLeft = shotsLeft;
    }
}
