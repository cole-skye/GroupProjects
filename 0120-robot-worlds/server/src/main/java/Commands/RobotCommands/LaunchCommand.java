package Commands.RobotCommands;

import Commands.RobotCommand;
import Robot.Robot;
import World.World;
import org.json.simple.JSONObject;

public class LaunchCommand extends RobotCommand {

    public LaunchCommand() {
        super("launch");
    }

   @Override
    public JSONObject execute(Robot target, World world) {
        if (!world.isRobotTypeInWorld(target)) {
            world.addRobot(target);
            return createResponse(target, "OK", "launch");
        }
       return createResponse(target, "ERROR", "launch");
    }

}
