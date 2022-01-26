package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.world.World.Search.CheckWorld;

import java.util.*;

/**
 * Calls on CheckWorld to look for world objects
 * and their types
 * @return true to continue playing
 */
public class LookCommand extends Command{

    ArrayList<Map<String, Object>> worldObjectData = new ArrayList<>();

    public LookCommand(){
        super("look");
    }

    @Override
    public boolean execute(Robot target) {
        worldObjectData = CheckWorld.doSearch(target);
        if (worldObjectData.toString().equals("[]")){
            target.setMessage("No objects visible");
        }
        else {
            target.setMessage(worldObjectData.toString());
        }
        return true;
    }
}
