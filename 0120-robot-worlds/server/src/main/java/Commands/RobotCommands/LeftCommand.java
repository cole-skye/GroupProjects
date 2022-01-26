package Commands.RobotCommands;

import Commands.RobotCommand;
import Robot.Robot;
import World.World;
import org.json.simple.JSONObject;

public class LeftCommand extends RobotCommand {

    @Override
    public JSONObject execute(Robot target, World world){
        target.updateDirection(false);
        target.setStatus(Robot.Status.NORMAL);
        return createResponse(target, "OK", "left");
    }

    public LeftCommand(){super("left");}
}
