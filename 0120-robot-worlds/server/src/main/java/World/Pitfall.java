package World;

import Robot.Position;
import Robot.Robot;
import Robot.Robot.Direction;

import java.util.List;

public class Pitfall {

    int bottomLeftX;
    int bottomLeftY;
    int topRightX;
    int topRightY;

    public Pitfall(int bottomLeftX, int bottomLeftY) {
        this.bottomLeftX = bottomLeftX;
        this.bottomLeftY = bottomLeftY;
        topRightX = bottomLeftX+4;
        topRightY = bottomLeftY+4;
    }

    public int getTopRightX() {
        return topRightX;
    }

    public int getTopRightY() {
        return topRightY;
    }

    public int getBottomLeftX() {
        return this.bottomLeftX;
    }

    public int getBottomLeftY() {
        return this.bottomLeftY;
    }

    public static int getSteps(Position robotPos, Pitfall pitfall, String direction) {
        switch (direction) {
            case "north":
                return (pitfall.getBottomLeftY() - robotPos.getY());
            case "south":
                return(robotPos.getY() - pitfall.getTopRightY())+1;
            case "west":
                return (robotPos.getX() - pitfall.getTopRightX())+1;
        }
        // east
        return (pitfall.getBottomLeftX() - robotPos.getX());
    }

    private static Position getVisibilityFinalDest(Position robotPos, Direction direction, int visibility) {
        int x;
        int y;
        int newX;
        int newY;

        x = robotPos.getX();
        y = robotPos.getY();
        newX = x;
        newY = y;
        switch (direction) {
            case NORTH:
                newY = y + visibility;
                break;
            case WEST:
                newX = x - visibility;
                break;
            case SOUTH:
                newY = y - visibility;
                break;
            case EAST:
                newX = x + visibility;
                break;
        }
        return new Position(newX, newY);
    }

    private static Robot.Direction convertStringToDirection(String direction) {
        switch (direction.toLowerCase()) {
            case "north":
                return Robot.Direction.NORTH;
            case "south":
                return Robot.Direction.SOUTH;
            case "west":
                return Robot.Direction.WEST;
        }
        return Robot.Direction.EAST;
    }

    public static Pitfall getNearestVisiblePitfall(String direction, World world, Robot robot) {
        int visibility;
        Position startDest;
        Position finalDest;
        List<Pitfall> pitfalls;
        int nearest;
        Pitfall nearestPitfall;
        int steps;

        visibility = Config.fileUtils.getVisibility();
        startDest = robot.getPosition();
        finalDest = getVisibilityFinalDest(startDest, convertStringToDirection(direction), visibility);
        pitfalls = world.getPitfalls();
        nearest = 200;
        nearestPitfall = null;
        for (Pitfall pitfall : pitfalls) {
            if (!(pitfall.inPitfall(startDest, finalDest))) continue;
            steps = getSteps(startDest, pitfall, direction);
            if (steps > 0) {
                if ((visibility >= steps) && (steps < nearest)) {
                    nearest = steps;
                    nearestPitfall = pitfall;
                }
            }
        }
        return nearestPitfall;
    }

    public boolean fallIn(Position position) {
        int x;
        int y;
        boolean holeX;
        boolean holeY;

        x = position.getX();
        y = position.getY();
        holeX = (x >= bottomLeftX) && (x <= topRightX);
        holeY = (y >= bottomLeftY) && (y <= topRightY);
        return (holeX && holeY);
    }


    public boolean inPitfall(Position a, Position b) {
        int startX;
        int startY;
        int endX;
        int endY;

        if (a.getX() == b.getX()) {
            startY = Math.min(a.getY(), b.getY());
            endY = Math.max(a.getY(), b.getY());
            for (int y = startY; y <= endY; y++) {
                if (fallIn(new Position(a.getX(), y))) return true;
            }
        } else {
            startX = Math.min(a.getX(), b.getX());
            endX = Math.max(a.getX(), b.getX());
            for (int x = startX; x <= endX; x++) {
                if (fallIn(new Position(x, a.getY()))) return true;
            }
        }
        return false;
    }

}
