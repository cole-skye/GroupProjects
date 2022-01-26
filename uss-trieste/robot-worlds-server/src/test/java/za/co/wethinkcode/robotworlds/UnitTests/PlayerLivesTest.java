package za.co.wethinkcode.robotworlds.UnitTests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Domain.Map.EmptyMap;
import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.RobotCommands.ForwardCommand;
import za.co.wethinkcode.Domain.RobotCommands.ShutdownCommand;
import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.WorldObjects.Pit.SquarePits;
import za.co.wethinkcode.Domain.world.World.World;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerLivesTest {

    @Test
    void isRobotAlive(){
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());
        assertEquals(true, robot.isAlive());

    }

    @Test
    void deathByPit() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));

        List<WorldObject> pits = new ArrayList<>();
        WorldObject pit = new SquarePits(0,1);
        pits.add(pit);

        SocketServer.getWorld().setPits(pits);

        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());
        robot.setPosition(Robot.CENTRE);
        robot.handleCommand(new ForwardCommand("5"));

        assertEquals(false, robot.isAlive());
        assertEquals("Fell", robot.getMessage());
        robot.handleCommand(new ShutdownCommand());
    }
}
