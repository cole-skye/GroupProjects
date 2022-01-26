package Commands;

import Commands.WorldCommands.DumpCommand;
import Commands.WorldCommands.PurgeCommand;
import Commands.WorldCommands.RobotCommand;
import Commands.WorldCommands.ShutdownCommand;
import ThreadManager.ClientHandler;
import World.World;

import java.util.ArrayList;

public abstract class WorldCommand {

    private final String name;
    private String argument;

    public WorldCommand(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }

    public WorldCommand(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }

    /**
     * Executes Command's logic.
     *
     * @return true if the Command is not the Shutdown Command.
     */
    public abstract boolean execute(World world);

    /**
     * Creates and retrieves the appropriate Command object based off of the given instruction by the server program.
     *
     * @return the appropriate Command object
     */
    public static WorldCommand create(String command, String arguments, ArrayList<ClientHandler> clients) {
        switch(command) {
            case "off":
            case "shutdown":
            case "quit":
                return new ShutdownCommand();
            case "purge":
                return new PurgeCommand(arguments, clients);
            case "dump":
                return new DumpCommand(clients);
            case "robot":
                return new RobotCommand();
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

}
