package za.co.wethinkcode.robotworlds.UnitTests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Domain.Map.EmptyMap;
import za.co.wethinkcode.Domain.Robot.Position;
import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.RobotCommands.*;
import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.World.IWorld;
import za.co.wethinkcode.Domain.world.World.World;
import za.co.wethinkcode.Domain.world.WorldObjects.Obstacle.SquareObstacle;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobotTests {

    @Test
    void initialPosition() {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());
        robot.setPosition(Robot.CENTRE);
        assertEquals(
                Robot.CENTRE, robot.getPosition());
        assertEquals(IWorld.Direction.NORTH, robot.getCurrentDirection());
    }

    @Test
    void shouldUpdatePositionAfterMovement() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());
        robot.setPosition(Robot.CENTRE);
        ForwardCommand command = new ForwardCommand("10");
        assertTrue(robot.handleCommand(command));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() + 10);
        assertEquals(expectedPosition, robot.getPosition());
        assertEquals("Done", robot.getMessage());
        robot.handleCommand(new ShutdownCommand());

    }

    @Test
    void shouldUpdateDirectionAfterTurning() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());
        robot.setPosition(Robot.CENTRE);
        Command command = new TurnCommand("right");
        assertTrue(robot.handleCommand(command));
        assertEquals("Done", robot.getMessage());
        assertEquals(IWorld.Direction.EAST, robot.getCurrentDirection());
        robot.handleCommand(new ShutdownCommand());

    }

    @Test
    void shouldFindNothingInEmptyWorldUsingLookCommand() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());
        Position spawnPosition = new Position(0, -30);

        robot.setPosition(spawnPosition);
        Command lookCommand = new LookCommand();

        assertTrue(robot.handleCommand(lookCommand));
        assertEquals("No objects visible", robot.getMessage());
        robot.handleCommand(new ShutdownCommand());
    }

    //TODO: make relative to Acceptance Tests. ( Jamie Task )

    @Test
    void shouldFindObstacleInWorldUsingLookCommand() throws IOException {
        // Use TestMaze
        SocketServer.setWorld(new World(new EmptyMap()));

        List<WorldObject> obstacleList = new ArrayList<>();
        WorldObject obstacle = new SquareObstacle(0, 1);
        obstacleList.add(obstacle);
        SocketServer.getWorld().setObstacles(obstacleList);

        Robot robot = new Robot("CrashTestDummy", SocketServer.getWorld());


        // Spawn robot at the bottom of obstacle [34,10]
        Position spawnPosition = Robot.CENTRE;
        robot.setPosition(spawnPosition);
        Command lookCommand = new LookCommand();

        assertTrue(robot.handleCommand(lookCommand));
        assertEquals("[{distance=1, type=OBSTACLE, direction=NORTH}]", robot.getMessage());
        robot.handleCommand(new ShutdownCommand());
    }

    //TODO: make relative to Acceptance Tests. ( Jamie Task )
    @Test
    void shouldFindRobotInWorldContainingRobotUsingLookCommand() throws IOException {
        SocketServer.setWorld(new World(new EmptyMap()));
        Robot searcher = new Robot("Searcher", SocketServer.getWorld());
        Robot foundRobot = new Robot("Found", SocketServer.getWorld());
        SocketServer.getWorld().addRobotToRobotList(searcher);
        SocketServer.getWorld().addRobotToRobotList(foundRobot);

        Position searchingRobotPosition = new Position(0, 0);
        Position foundRobotPosition = new Position(0, 10);

        String expectedOutput = "[{distance=10, type=ROBOT, direction=NORTH}]";

        searcher.setPosition(searchingRobotPosition);
        foundRobot.setPosition(foundRobotPosition);

        Command look = new LookCommand();

        assertTrue(searcher.handleCommand(look));
        assertEquals(expectedOutput, searcher.getMessage());

        // remove searcher from world
        searcher.handleCommand(new ShutdownCommand());

        // remove foundRobot from world
        foundRobot.handleCommand(new ShutdownCommand());

    }
}
