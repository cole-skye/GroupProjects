package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.Robot.ScheduleTimer;
import za.co.wethinkcode.Socket.SocketServer;

/**
 * Repair the robot's shields
 * and set status to repairing
 * @return true to continue playing
 */
public class RepairCommand extends Command{

    public RepairCommand() {
        super("repair");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("REPAIRING");
        new ScheduleTimer(target, "repair", SocketServer.configReader.getRepairTime());

        target.setMessage("Repairing Shield.");
        return true;
    }
}
