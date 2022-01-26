import Robot.*;
import World.World;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorldTest {

    @Test
    public void getRobotFromWorld() {
        World world = new World();
        Robot a = new Robot("CrashTestDummy", "normal", 0, 0);
        world.addRobot(a);
        assertEquals(world.getRobot(a).getName(), "CrashTestDummy");
    }

}
