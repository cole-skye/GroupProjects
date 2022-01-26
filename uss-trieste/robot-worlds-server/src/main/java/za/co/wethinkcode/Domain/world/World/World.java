package za.co.wethinkcode.Domain.world.World;

import za.co.wethinkcode.Domain.Map.Map;
import za.co.wethinkcode.Domain.world.World.AbstractWorld;

public class World extends AbstractWorld {

    public World(Map map){
        super(map);
    }

    public void showObstacles() {
        System.out.println(this.toString());
    }
}
