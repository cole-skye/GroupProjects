package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private final String name;
    private String argument;
    private List<String> commandHistory = new ArrayList<>();
    public abstract boolean execute(Robot target) throws IOException;

    /**
     * Adds a command to the history list for the replay command
     * @param instruction
     */
    public void addHistory(String instruction){
        this.commandHistory.add(instruction);
    }

    /**
     * initialises a command with only a name and no argument
     * argument get defaulted to ""
     * @param name
     */
    public Command(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }

    /**
     * initialises a command with a name and argument
     * @param name
     * @param argument
     */
    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    /**
     * Command name
     * @return name of the command
     */
    public String getName() {                                                                           //<2>
        return name;
    }

    /**
     * Command argument
     * @return argument passed with the command
     */
    public String getArgument() {
        return this.argument;
    }

    /**
     * creates a command to execute for the specific Robot
     * @param instruction
     * @return a new Command for the robot to handle
     */
    public static Command create(String instruction) {
        String[] args = instruction.toLowerCase().trim().split(" ");
        switch (args[0]){
            case "shutdown":
            case "off":
                return new ShutdownCommand();
            case "forward":
                return new ForwardCommand(args[1]);
            case "back":
                return new BackCommand(args[1]);
            case "sprint":
                return new SprintCommand(args[1]);
            case "turn":
                return new TurnCommand(args[1]);
            case "look":
                return new LookCommand();
            case "launch":
                return new HelpCommand();
            case "repair":
                return new RepairCommand();
            case "mine":
                return new DeployMineCommand();
            default:
                throw new IllegalArgumentException("Unsupported command: " + args[0]);
        }
    }
}
