package za.co.wethinkcode.robotworlds.UnitTests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Domain.world.World.IWorld.Direction;

import static org.junit.jupiter.api.Assertions.*;

class DirectionsTest {
    @Test
    void testNorth(){
        assertEquals("NORTH", Direction.NORTH.toString());
    }

    @Test
    void testEast(){
        assertEquals("EAST", Direction.EAST.toString());
    }

    @Test
    void testSouth(){
        assertEquals("SOUTH", Direction.SOUTH.toString());
    }

    @Test
    void testWest(){
        assertEquals("WEST", Direction.WEST.toString());
    }
}
