package Commands.WorldCommands;

import Commands.WorldCommand;
import JsonUtils.JsonEncode;
import Robot.Robot;
import ThreadManager.ClientHandler;
import World.World;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class PurgeCommand extends WorldCommand {
    private ArrayList<ClientHandler> clients;

    public PurgeCommand(String arguments, ArrayList<ClientHandler> clients) {
        super("purge", arguments);
        this.clients = clients;
    }

    @Override
    public boolean execute(World world) {
        JSONObject response;
        Robot robot;

        robot = world.getRobotWithName(getArgument());
        if (robot == null) {
            world.setStatus("<WORLD> Robot, with specified name, does not exist in world.");
        } else {
            robot.setStatus(Robot.Status.DEAD);
            response = executePurge(robot);
            sendResponseToClient(robot, response);
            world.getAllRobots().remove(robot);
            world.setStatus("<WORLD> Robot successfully removed from the world.");
        }
        return true;
    }

    private void sendResponseToClient(Robot robot, JSONObject response) {
        for (ClientHandler client: clients) {
            if (client.getRobot().equals(robot)) {
                client.sendToClient(response);
                System.out.println("SENT");
                return;
            }
        }
    }

    private JSONObject executePurge(Robot target){
        JsonEncode encoder;
        String result;

        result = "OK";
        encoder = new JsonEncode(target, result, "purge");
        encoder.setStatus("DEAD");
        encoder.setMessage("PURGED");
        return encoder.response;
    }

}
