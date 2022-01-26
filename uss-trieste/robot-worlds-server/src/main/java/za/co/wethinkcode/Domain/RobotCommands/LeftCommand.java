package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.world.World.IWorld;

/**
 * change robot direction
 * updates status
 * @return true to continue playing
 */
public class LeftCommand extends Command {

    @Override
    public boolean execute(Robot target) {
        IWorld.Direction direction = target.getCurrentDirection();

        switch (direction){
            case NORTH: {
                target.setCurrentDirection(IWorld.Direction.WEST);
                break;
            }
            case WEST: {
                target.setCurrentDirection(IWorld.Direction.SOUTH);
                break;
            }
            case SOUTH: {
                target.setCurrentDirection(IWorld.Direction.EAST);
                break;
            }
            case EAST: {
                target.setCurrentDirection(IWorld.Direction.NORTH);
                break;
            }
        }
        target.setMessage("Done");
        this.addHistory("left");
        return true;
    }

    public LeftCommand() {
        super("left");
    }
}
