package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Socket.SocketServer;

/**
 * kills robot and sets status to DEAD
 * sets message to shutting down
 * @return true to continue playing
 */
public class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("off");
    }

    @Override
    public boolean execute(Robot target) {
        target.setMessage("Shutting down...");
        target.setStatus("DEAD");
        SocketServer.getWorld().removeRobotFromRobotList(target.getName());
        return false;
    }
}
