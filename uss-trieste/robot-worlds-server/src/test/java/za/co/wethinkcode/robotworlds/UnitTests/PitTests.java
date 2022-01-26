package za.co.wethinkcode.robotworlds.UnitTests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Domain.Map.EmptyMap;
import za.co.wethinkcode.Domain.Robot.Position;
import za.co.wethinkcode.Domain.world.World.AbstractWorld;
import za.co.wethinkcode.Domain.world.World.World;
import za.co.wethinkcode.Domain.world.WorldObjects.Pit.SquarePits;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;


import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PitTests {

    @Test
    void getBottomLeftX(){
        WorldObject block = new SquarePits(5, 5);
        assertEquals("5", Integer.toString(block.getX()));
    }

    @Test
    void testBlockPosition(){
        AbstractWorld world = new World(new EmptyMap());
        List<WorldObject> pitList = world.getPits();
        WorldObject pits = new SquarePits(1, 1);
        pitList.add(pits);
        WorldObject pit = pitList.get(0);
        assertTrue(pit.encounterObjectPos(new Position(1,1)));
        assertFalse(pit.encounterObjectPos(new Position(0,5)));
        assertFalse(pit.encounterObjectPos(new Position(6,5)));
    }

    @Test
    void testPitInPath(){
        World world = new World(new EmptyMap());
        List<WorldObject> pitList = world.getPits();
        WorldObject pits = new SquarePits(1, 1);
        pitList.add(pits);
        WorldObject pit = pitList.get(0);
        assertTrue(pit.encounterObjectInPath(new Position(1,0), new Position(1,50)));
        assertFalse(pit.encounterObjectInPath(new Position(0,1), new Position(0,100)));

    }
}
