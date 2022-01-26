package za.co.wethinkcode.robotworlds.UnitTests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Domain.Map.EmptyMap;
import za.co.wethinkcode.Domain.Robot.Position;
import za.co.wethinkcode.Domain.world.World.World;
import za.co.wethinkcode.Domain.world.WorldObjects.Obstacle.SquareObstacle;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ObstacleTests {

    @Test
    void obstacleX(){
        World world = new World(new EmptyMap());
        List<WorldObject> obstacleList = world.getObstacles();

        SquareObstacle block = new SquareObstacle(10, 10);
        obstacleList.add(block);
        assertEquals("10", Integer.toString(obstacleList.get(0).getX()));
    }

    @Test
    void obstacleBottomLeftY(){
        SquareObstacle block = new SquareObstacle(10, 10);
        assertEquals("10", Integer.toString(block.getY()));
    }

    @Test
    void testBlockPosition(){
        World world = new World(new EmptyMap());
        List<WorldObject> obstacleList = world.getObstacles();
        WorldObject obstacles = new SquareObstacle(1, 1);
        obstacleList.add(obstacles);
        WorldObject obstacle = obstacleList.get(0);
        assertTrue(obstacle.encounterObjectPos(new Position(1,1)));
        assertFalse(obstacle.encounterObjectPos(new Position(0,5)));
    }

    @Test
    void testBlockPath(){
        World world = new World(new EmptyMap());
        List<WorldObject> obstacleList = world.getObstacles();
        WorldObject obstacles = new SquareObstacle(1, 1);
        obstacleList.add(obstacles);
        WorldObject obstacle = obstacleList.get(0);
        assertTrue(obstacle.encounterObjectInPath(new Position(1,0), new Position(1,50)));
        assertFalse(obstacle.encounterObjectInPath(new Position(0,1), new Position(0, 100)));

    }
}
