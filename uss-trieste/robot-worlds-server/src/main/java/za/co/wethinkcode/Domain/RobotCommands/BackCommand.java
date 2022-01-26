package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;

/**
 * move robot back in direction
 * updates status
 * @return true to continue playing
 */
public class BackCommand extends Command {
    @Override
    public boolean execute(Robot target) {
        String newArgument = getArgument().replace("[\"", "");
        newArgument = newArgument.replace("\"]", "");
        int nrSteps = Integer.parseInt(newArgument);
        if (target.updatePosition(-nrSteps)) {
            target.setMessage("Done");
        }
        this.addHistory("back " + getArgument());
        return true;
    }

    public BackCommand(String argument) {
        super("back", argument);
    }
}
