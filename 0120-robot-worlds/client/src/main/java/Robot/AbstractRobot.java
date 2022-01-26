package Robot;

public class AbstractRobot implements IRobot {

    private final String name;
    private final String type;
    private Position position;
    private int shieldStrength;
    private int gunDistance;
    private int maxShots;
    private Direction direction;
    private String status;

    public AbstractRobot(String name, String type) {
        this.name = name;
        this.type = type;
        shieldStrength = 0;
        gunDistance = 0;
        maxShots = 0;
        direction = Direction.NORTH;
        setStatus("Ready!");
    }

    /**
     * Retrieves the Robot object's name.
     *
     * @return      the name of the Robot object.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the Robot object's Position object.
     *
     * @return      the Position object of the Robot object.
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Retrieves the Robot object's type.
     *
     * @return      the type of the Robot object.
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Retrieves the Robot object's shield strength.
     *
     * @return      the shield strength of the Robot object.
     */
    @Override
    public int getShieldStrength() {
        return shieldStrength;
    }

    /**
     * Retrieves the Robot object's maximum gun shots.
     *
     * @return      the maximum gun shots of the Robot object.
     */
    @Override
    public int getMaxShots() {
        return maxShots;
    }

    /**
     * Updates the Robot's current Position object, with the new Position object.
     *
     * @param  position    the new Position object to assign to the Robot object.
     */
    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Updates the Robot's current Direction enum value, with the new Direction enum value.
     *
     * @param  direction    the new Direction enum value to assign to the Robot object.
     */
    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * Updates the Robot's shield strength value, with the new shield strength value.
     *
     * @param  shieldStrength    the new shield strength value to assign to the Robot object.
     */
    public void setShieldStrength(int shieldStrength) {
        this.shieldStrength = shieldStrength;
    }

    /**
     * Updates the Robot's maximum gun shot value, with the new maximum gun shot value.
     *
     * @param  maxShots    the new maximum gun shot value to assign to the Robot object.
     */
    public void setMaxShots(int maxShots) {
        this.maxShots = maxShots;
    }

    @Override
    public void setStatus(String status) {
        if (position != null) {
            this.status = "<"+getPosition().getX()+","+getPosition().getY()+"> "+getName()+" : "+status;
        } else {
            this.status = "> "+getName()+" : "+status;
        }
    }

    @Override
    public String getStatus() {
        return status;
    }
}
