package Robot;

public interface IRobot {

    /**
     * Retrieve the Robot object's name.
     */
    String getName();

    /**
     * Retrieve the Robot object's type.
     */
    String getType();

    /**
     * Retrieve the Robot object's Position object.
     */
    Position getPosition();

    /**
     * Retrieve the Robot object's shield strength.
     */
    int getShieldStrength();

    /**
     * Retrieve the Robot object's maximum gun shots.
     */
    int getMaxShots();

    /**
     * Updates the Robot's current Position object, with the new Position object.
     *
     * @param  position    the new Position object to assign to the Robot object.
     */
    void setPosition(Position position);

    /**
     * Updates the Robot's current Direction enum value, with the new Direction enum value.
     *
     * @param  direction    the new Direction enum value to assign to the Robot object.
     */
    void setDirection(Direction direction);

    /**
     * Updates the Robot's shield strength value, with the new shield strength value.
     *
     * @param  shieldStrength    the new shield strength value to assign to the Robot object.
     */
    void setShieldStrength(int shieldStrength);

    /**
     * Updates the Robot's maximum gun shot value, with the new maximum gun shot value.
     *
     * @param  maxShots    the new maximum gun shot value to assign to the Robot object.
     */
    void setMaxShots(int maxShots);

    void setStatus(String status);

    Direction getDirection();

    String getStatus();

    /**
     * Enum used to track direction
     */
    enum Direction {
        NORTH, WEST, SOUTH, EAST
    }

}
