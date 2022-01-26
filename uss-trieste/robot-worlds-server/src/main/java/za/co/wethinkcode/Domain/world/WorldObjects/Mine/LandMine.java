package za.co.wethinkcode.Domain.world.WorldObjects.Mine;

import za.co.wethinkcode.Domain.Robot.Position;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

/**
 * Land mine that inflicts damage to robots that encounters one
 */
public class LandMine implements WorldObject {

    private final int x;
    private final int y;
    private final String type;

    public LandMine(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = "MINE";

    }

    /**
     * @return Gets the x value of a mine.
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * @return Gets the y value of a mine.
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * @return Mine type
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * @return String of robot position
     */
    public String getAsString() {
        return "["+this.x+","+this.y+"]";
    }

    /**
     * Checks robot position with indexed mine in mines list.
     * @param position robot position
     * @return true if mine is at position.
     */
    @Override
    public boolean encounterObjectPos(Position position) {
        boolean xOnObstacle = this.x == position.getX();
        boolean yOnObstacle = this.y == position.getY();
        return xOnObstacle && yOnObstacle;
    }

    /**
     * checks robot position will all the position of mines
     * in the mines list.
     * @param a old position
     * @param b new position
     * @return true if mines is at position.
     */
    @Override
    public boolean encounterObjectInPath(Position a, Position b) {
        if (a.getY() == b.getY()) {
            for (int i = Math.min(a.getX(), b.getX()); i <= Math.max(a.getX(), b.getX()); i++) {
                if (encounterObjectPos(new Position(i, b.getY()))) {
                    return true;
                }
            }
        } else if (a.getX() == b.getX()) {
            for (int j = Math.min(a.getY(), b.getY()); j <= Math.max(a.getY(), b.getY()); j++) {
                if (encounterObjectPos(new Position(b.getX(), j))) {
                    return true;
                }
            }
        }
        return false;
    }
}
