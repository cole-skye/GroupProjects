package za.co.wethinkcode.Domain.Map;

import za.co.wethinkcode.Domain.Configuration.ConfigReader;
import za.co.wethinkcode.Domain.world.WorldObjects.Obstacle.SquareObstacle;
import za.co.wethinkcode.Domain.world.WorldObjects.Pit.SquarePits;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

public class TestMap extends AbstractMap {
    private final ConfigReader configReader = new ConfigReader();

    public TestMap() {
        this.generateObstacles();
        setName("Test");
    }

    String[] maze = new String[]{

            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "              *                  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",

    };

    /**
     * Iterates through a list called maze.
     * Generates obstacles if a "*" is encountered.
     * Generates a pit if a "P" is encountered.
     */
    public void generateObstacles() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length(); j++) {
                char charIndex = maze[i].charAt(j);
                String character = String.valueOf(charIndex);

                int screen_x = (-(configReader.getWorldWidth()/2) /2) + (j * (int) 6.5);
                int screen_y = ((configReader.getWorldHeight()/2) /2) - (i * 18);


                if (character.equals("*")) {
                    WorldObject obstacle = new SquareObstacle(screen_x, screen_y);

                    addObstacle(obstacle);

                }
                if (character.equals("P")) {
                    WorldObject pit = new SquarePits(screen_x, screen_y);

                    addPit(pit);

                }
            }
        }
    }
}
