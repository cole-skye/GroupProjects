package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;

import java.io.IOException;

/**
 * calls a direction to turn to with argument
 * "left" or "right"
 * @return true to continue playing
 */
public class TurnCommand extends Command {
    @Override
    public boolean execute(Robot target) throws IOException {
        String newArgument = getArgument().replace("[\"", "");
        newArgument = newArgument.replace("\"]", "");
        String direction = newArgument.trim().toLowerCase();
        switch (direction) {
            case "right":
                return target.handleCommand(new RightCommand());
            case "left":
                return target.handleCommand(new LeftCommand());
            default:
                throw new IllegalArgumentException("Unsupported command: " + direction);
        }
    }
    public TurnCommand(String argument) {
        super("turn", argument);
    }

}
