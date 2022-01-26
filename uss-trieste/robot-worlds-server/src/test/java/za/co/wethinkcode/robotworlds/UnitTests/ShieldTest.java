package za.co.wethinkcode.robotworlds.UnitTests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Domain.Map.EmptyMap;
import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.RobotCommands.Command;
import za.co.wethinkcode.Domain.RobotCommands.ForwardCommand;
import za.co.wethinkcode.Domain.RobotCommands.ShutdownCommand;
import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.World.World;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ShieldTest {

    @Test
    void shieldStartWithConfigValue() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("jeff", new World(new EmptyMap()));
        robot.setPosition(Robot.CENTRE);
        robot.setNumberOfMines(5);
        int shield = robot.getShields();

        assertEquals(SocketServer.configReader.getMaxShield(), shield);
        robot.handleCommand(new ShutdownCommand());

    }

    @Test
    void loseShieldToMine() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("jeff", new World(new EmptyMap()));
        robot.setPosition(Robot.CENTRE);
        robot.setNumberOfMines(5);
        robot.setShieldStrength(7);

        robot.handleCommand(new ForwardCommand("2"));

        int[] expectedPosition4 = new int[] {Robot.CENTRE.getX(), Robot.CENTRE.getY() + 2};
        assertEquals(Arrays.toString(expectedPosition4), Arrays.toString(new int[]{robot.getPosition().getX(), robot.getPosition().getY()}));

        Command mine = Command.create("mine");
        assertTrue(mine.execute(robot));
        assertEquals("Deploying Mine, mines left: 4", robot.getMessage());

        Command back = Command.create("back 3");
        assertTrue(back.execute(robot));

        int[] expectedPosition2 = new int[] {Robot.CENTRE.getX(), Robot.CENTRE.getY() + 2};
        assertEquals(Arrays.toString(expectedPosition2), Arrays.toString(new int[]{robot.getPosition().getX(), robot.getPosition().getY()}));

        assertEquals(4, robot.getShields());
        robot.handleCommand(new ShutdownCommand());

    }

    @Test
    void breakShield() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("jeff", new World(new EmptyMap()));
        robot.setPosition(Robot.CENTRE);
        robot.setNumberOfMines(1);
        robot.setShieldStrength(3);

        robot.handleCommand(new ForwardCommand("2"));

        int[] expectedPosition4 = new int[] {Robot.CENTRE.getX(), Robot.CENTRE.getY() + 2};
        assertEquals(Arrays.toString(expectedPosition4), Arrays.toString(new int[]{robot.getPosition().getX(), robot.getPosition().getY()}));

        Command mine = Command.create("mine");
        assertTrue(mine.execute(robot));
        assertEquals("Deploying Mine, mines left: 0", robot.getMessage());

        Command back = Command.create("back 3");
        assertTrue(back.execute(robot));

        int[] expectedPosition2 = new int[] {Robot.CENTRE.getX(), Robot.CENTRE.getY() + 2};
        assertEquals(Arrays.toString(expectedPosition2), Arrays.toString(new int[]{robot.getPosition().getX(), robot.getPosition().getY()}));

        assertEquals(0, robot.getShields());
        robot.handleCommand(new ShutdownCommand());

    }

}
