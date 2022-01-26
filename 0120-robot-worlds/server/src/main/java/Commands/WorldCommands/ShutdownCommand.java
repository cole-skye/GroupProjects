package Commands.WorldCommands;

import Commands.WorldCommand;
import World.World;

public class ShutdownCommand extends WorldCommand {

    public ShutdownCommand() {
        super("off");
    }

    @Override
    public boolean execute(World world) {
        world.setStatus("<WORLD> Shutting down...");
        return false;
    }
}
