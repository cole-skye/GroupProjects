package za.co.wethinkcode.Domain.world.WorldObjects.Obstacle;

import za.co.wethinkcode.Domain.Robot.Position;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

/**
 * obstacle that obstructs the robots path
 */
public class SquareObstacle implements WorldObject {

    private final int x;
    private final int y;
    public final String type = "OBSTACLE";


    public SquareObstacle(int x, int y){
        this.x= x;
        this.y = y;
    }

    /**
     * @return Obstacle type
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * @return Gets the x value of a Obstacle.
     */
    @Override
    public int getX() {
        return this.x;
    }

    /**
     * @return Gets the y value of a Obstacle.
     */
    @Override
    public int getY() {
        return this.y;
    }

    /**
     * @return String of robot position
     */
    public String getAsString() {
        return "["+this.x+","+this.y+"]";
    }

    /**
     * Checks robot position with indexed obstacle in obstacle list.
     * @param position robot position
     * @return true if obstacle is at position.
     */
    @Override
    public boolean encounterObjectPos(Position position) {
        boolean xOnObstacle = this.x == position.getX();
        boolean yOnObstacle = this.y == position.getY();
        return xOnObstacle && yOnObstacle;
    }

    /**
     * checks robot position will all the position of obstacles
     * in the obstacle list.
     * @param a old position
     * @param b new position
     * @return true if obstacle is at position else it will return false.
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
