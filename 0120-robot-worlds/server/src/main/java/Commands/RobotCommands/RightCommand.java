package Commands.RobotCommands;

import Commands.RobotCommand;
import Robot.Robot;
import World.World;
import org.json.simple.JSONObject;

public class RightCommand extends RobotCommand {

    @Override
    public JSONObject execute(Robot target, World world){
        target.updateDirection(true);
        target.setStatus(Robot.Status.NORMAL);
        return createResponse(target, "OK", "right");
    }

    public RightCommand(){super("right");}
}