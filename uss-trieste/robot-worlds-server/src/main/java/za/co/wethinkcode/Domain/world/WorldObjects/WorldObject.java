package za.co.wethinkcode.Domain.world.WorldObjects;

import za.co.wethinkcode.Domain.Robot.Position;

public interface WorldObject {

    int getX();

    int getY();

    String getType();

    String getAsString();

    boolean encounterObjectPos(Position position);

    boolean encounterObjectInPath(Position position, Position newPosition);


}
