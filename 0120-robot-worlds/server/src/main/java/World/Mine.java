package World;

import Robot.Position;

public class Mine {

    int bottomLeftX;
    int bottomLeftY;
    int topRightX;
    int topRightY;
    boolean isActive;

    public Mine(int bottomLeftX, int bottomLeftY) {
        this.bottomLeftX = bottomLeftX;
        this.bottomLeftY = bottomLeftY;
        topRightX = bottomLeftX+4;
        topRightY = bottomLeftY+4;
        isActive = false;
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

    public void Activate(){
        isActive = true;
    }

    public boolean getIsActive(){
        return isActive;
    }

    public boolean mineInPosition(Position position) {
        int x;
        int y;
        boolean holeX;
        boolean holeY;

        if (!isActive) return false;
        x = position.getX();
        y = position.getY();
        holeX = (x >= bottomLeftX) && (x <= topRightX);
        holeY = (y >= bottomLeftY) && (y <= topRightY);
        return (holeX && holeY);
    }


    public boolean mineInPath(Position a, Position b) {
        int startX;
        int startY;
        int endX;
        int endY;

        if (a.getX() == b.getX()) {
            startY = Math.min(a.getY(), b.getY());
            endY = Math.max(a.getY(), b.getY());
            for (int y = startY; y <= endY; y++) {
                if (mineInPosition(new Position(a.getX(), y))) return true;
            }
        } else {
            startX = Math.min(a.getX(), b.getX());
            endX = Math.max(a.getX(), b.getX());
            for (int x = startX; x <= endX; x++) {
                if (mineInPosition(new Position(x, a.getY()))) return true;
            }
        }
        return false;
    }
}
