package za.co.wethinkcode.robotworlds.UnitTests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Domain.Map.EmptyMap;
import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.RobotCommands.Command;
import za.co.wethinkcode.Domain.RobotCommands.ShutdownCommand;
import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.World.World;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MineTests {

    @Test
    void CommandCreate(){
        Command mine = Command.create("mine");                                                          //<3>
        assertEquals("mine", mine.getName());
    }

    @Test
    void executeMineDeployment() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());
        robot.setPosition(Robot.CENTRE);
        Command mine = Command.create("mine");
        assertTrue(mine.execute(robot));

        assertEquals("Deploying Mine, mines left: 4", robot.getMessage());
        robot.handleCommand(new ShutdownCommand());

    }

    @Test
    void trippedMine() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());
        robot.setPosition(Robot.CENTRE);

        Command mine = Command.create("mine");
        assertTrue(mine.execute(robot));

        assertEquals("Deploying Mine, mines left: 4", robot.getMessage());

        Command back = Command.create("back 3");
        assertTrue(back.execute(robot));

        int[] expectedPosition2 = new int[] {Robot.CENTRE.getX(), Robot.CENTRE.getY()};
        assertEquals(Arrays.toString(expectedPosition2), Arrays.toString(new int[]{robot.getPosition().getX(), robot.getPosition().getY()}));

        robot.handleCommand(new ShutdownCommand());

    }



}
