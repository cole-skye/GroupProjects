package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;

/**
 * moves robot forward
 * updates status
 * @return true to continue playing
 */
public class ForwardCommand extends Command {
    @Override
    public boolean execute(Robot target) {
        String newArgument = getArgument().replace("[\"", "");
        newArgument = newArgument.replace("\"]", "");
        int nrSteps = Integer.parseInt(newArgument);
        if (target.updatePosition(nrSteps)){
            target.setMessage("Done");
        }
        this.addHistory("forward " + getArgument());
        return true;
    }

    public ForwardCommand(String argument) {
        super("forward", argument);
    }
}
