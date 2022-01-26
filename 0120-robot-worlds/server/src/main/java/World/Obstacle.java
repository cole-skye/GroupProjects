package World;

import Robot.Position;
import Robot.Robot;
import Robot.Robot.Direction;

import java.util.List;

public class Obstacle {
    int bottomLeftX;
    int bottomLeftY;
    int topRightX;
    int topRightY;

    public Obstacle(int bottomLeftX, int bottomLeftY) {
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

    public static int getSteps(Position robotPos, Obstacle obstacle, String direction) {
        switch (direction) {
            case "north":
                return (obstacle.getBottomLeftY() - robotPos.getY());
            case "south":
                return(robotPos.getY() - obstacle.getTopRightY())+1;
            case "west":
                return (robotPos.getX() - obstacle.getTopRightX())+1;
        }
        // east
        return (obstacle.getBottomLeftX() - robotPos.getX());
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

    public static Obstacle getNearestVisibleObstacle(String direction, World world, Robot robot) {
        int visibility;
        Position startDest;
        Position finalDest;
        List<Obstacle> obstacles;
        int nearest;
        Obstacle nearestObstacle;
        int steps;

        visibility = Config.fileUtils.getVisibility();
        startDest = robot.getPosition();
        finalDest = getVisibilityFinalDest(startDest, convertStringToDirection(direction), visibility);
        obstacles = world.getObstacles();
        nearest = 200;
        nearestObstacle = null;
        for (Obstacle obstacle : obstacles) {
            if (!(obstacle.blockPath(startDest, finalDest))) continue;
            steps = getSteps(startDest, obstacle, direction);
            if (steps > 0) {
                if ((visibility >= steps) && (steps < nearest)) {
                    nearest = steps;
                    nearestObstacle = obstacle;
                }
            }
        }
        return nearestObstacle;
    }

    public boolean blockPosition(Position position) {
        int x;
        int y;

        x = position.getX();
        y = position.getY();
        return (((x >= bottomLeftX) && (x <= topRightX)) && ((y >= bottomLeftY) && (y <= topRightY)));
    }


    public boolean blockPath(Position a, Position b) {
        int startX;
        int startY;
        int endX;
        int endY;

        if (a.getX() == b.getX()) {
            startY = Math.min(a.getY(), b.getY());
            endY = Math.max(a.getY(), b.getY());
            for (int y = startY; y <= endY; y++) {
                if (blockPosition(new Position(a.getX(), y))) return true;
            }
        } else {
            startX = Math.min(a.getX(), b.getX());
            endX = Math.max(a.getX(), b.getX());
            for (int x = startX; x <= endX; x++) {
                if (blockPosition(new Position(x, a.getY()))) return true;
            }
        }
        return false;
    }

}
