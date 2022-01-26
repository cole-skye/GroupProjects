package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;

/**
 * Moves forward in an sum of n: n-1 fashion, recursively
 * eg: forward 5, 4, 3, 2, 1. Position (0,15).
 * @return true to continue playing
 */
public class SprintCommand extends Command {

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        target.setMessage("");
        for (int i = nrSteps; i > 0 ; i--) {

            if (target.updatePosition(i)) {
                if (i == 1) {
                    target.setMessage("Moved forward by " + i + " steps.");
                } else {
                    System.out.println(target.toString() + "Moved forward by " + i + " steps.");
                }
            } else {
                target.setMessage("Sorry, I cannot go outside my safe zone.");
            }

        }

        this.addHistory("sprint " + getArgument());
        return true;
    }

    public SprintCommand(String argument) {
        super("sprint", argument);
    }
}
