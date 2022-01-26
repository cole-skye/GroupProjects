package Commands.WorldCommands;

import Commands.WorldCommand;
import Robot.Robot;
import World.World;

import java.util.List;

public class RobotCommand extends WorldCommand {

    public RobotCommand(){super("robot");}

    @Override
    public boolean execute(World world) {

        List<Robot> robots;
        String status;

        status = "";
        robots = world.getAllRobots();
        if (robots.size() == 0) {
            status = "There are currently no robots in the world.";
        }
        for (Robot robot : robots) {
            status += "Name: "+robot.getName()+" \n";
        }
        world.setStatus(status);
        return true;
    }
}
