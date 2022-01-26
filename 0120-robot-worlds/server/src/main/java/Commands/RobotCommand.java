package Commands;

import Commands.RobotCommands.*;
import Commands.RobotCommands.MineCommand.MineCommand;
import JsonUtils.JsonEncode;
import Robot.*;
import ThreadManager.ClientHandler;
import World.World;
import org.json.simple.JSONObject;

public abstract class RobotCommand {

    private final String name;
    private String argument;

    public RobotCommand(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }

    public RobotCommand(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    /**
     * Executes Command's logic.
     *
     * @return true if the Command is not the Shutdown Command.
     */
    public abstract JSONObject execute(Robot target, World world);

    /**
     * Creates and retrieves the appropriate Command object based off of the given instruction by the client program.
     *
     * @return the appropriate Command object
     */
    public static RobotCommand create(String command, String arguments) {
        switch(command) {
            case "right":
                return new RightCommand();
            case "left":
                return new LeftCommand();
            case "forward":
                return new ForwardCommand(arguments);
            case "back":
                return new BackCommand(arguments);
            case "launch":
                return new LaunchCommand();
            case "look":
                return new LookCommand();
            case "mine":
                return new MineCommand();
            case "fire":
                return new FireCommand(ClientHandler.clients);
            default:
                throw new IllegalArgumentException("Sorry, I did not understand: '"+command+" "+arguments+"'");
        }
    }

    /**
     * Retrieves the Command object's allocated name.
     *
     * @return the Command object's name
     */
    public String getName() {                                                                           //<2>
        return name;
    }

    /**
     * Retrieves the Command object's argument.
     *
     * @return the Command object's argument
     */
    public String getArgument() {
        return this.argument;
    }

    /**
     * Updates the Command object's argument value to the given argument value
     */
    public void setArgument(String argument) {
        this.argument = argument;
    }

    public JSONObject createResponse(Robot target, String result, String command) {
        JsonEncode encoder = new JsonEncode(target, result, command);
        return encoder.response;
    }

}
