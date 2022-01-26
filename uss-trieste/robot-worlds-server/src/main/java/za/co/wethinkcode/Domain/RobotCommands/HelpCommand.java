package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(Robot target) {
        target.setMessage(" > Ready");

//        target.setMessage("I can understand these commands:\n" +
//                "OFF  - Shut down robot\n" +
//                "HELP - provide information about commands\n" +
//                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n"+
//                "BACK - move back by specified number of steps, e.g. 'BACK 10'\n"+
//                "TURN RIGHT - turn right by 90 degrees\n"+
//                "TURN LEFT - turn left by 90 degrees\n"+
//                "SPRINT - sprint forward according to a formula");
        return true;
    }
}
