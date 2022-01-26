package za.co.wethinkcode.Domain.Map;

import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.WorldObjects.Obstacle.SquareObstacle;
import za.co.wethinkcode.Domain.world.WorldObjects.Pit.SquarePits;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMap extends AbstractMap {

    public RandomMap() {
        super();
        super.setName("Random");
        super.addObstacles(this.generateObstacles());
    }

    /**
     * Populate a list with obstacles
     * @return list of obstacles
     */
    public List<WorldObject> generateObstacles(){
        int size, x, y;
        Random random = new Random();

        List<WorldObject> pits = new ArrayList<>();
        List<WorldObject> obstacles = new ArrayList<>();
        size = random.nextInt(11) + 9;
        for (int i = 0; i < size/2; i++){
            x = random.nextInt(SocketServer.configReader.getWorldWidth() + 1) - 100;
            y = random.nextInt(SocketServer.configReader.getWorldHeight() + 1) - 200;
            pits.add(new SquarePits(x, y));
        }

        for (int i = 0; i < size; i++){
            x = random.nextInt(SocketServer.configReader.getWorldWidth() + 1) - 100;
            y = random.nextInt(SocketServer.configReader.getWorldHeight() + 1) - 200;
            obstacles.add(new SquareObstacle(x, y));
        }

        return obstacles;
    }
}
