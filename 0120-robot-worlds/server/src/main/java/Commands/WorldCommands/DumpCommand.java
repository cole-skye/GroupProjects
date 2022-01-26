package Commands.WorldCommands;

import Commands.WorldCommand;
import JsonUtils.JsonEncode;
import Robot.Robot;
import ThreadManager.ClientHandler;
import World.World;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DumpCommand extends WorldCommand {
    private ArrayList<ClientHandler> clients;

    public DumpCommand(ArrayList<ClientHandler> clients){
        super("dump");
        this.clients = clients;
    }

    @Override
    public boolean execute(World world) {
        JSONObject response;

        for (Robot robot: world.getAllRobots()) {
            response = executeDump(robot);
            sendResponseToClient(robot, response);
        }
        world.resetRobotsList();
        world.setStatus("<WORLD> All robots successfully removed.");
        return true;
    }

    private void sendResponseToClient(Robot robot, JSONObject response) {
        for (ClientHandler client: clients) {
            if (client.getRobot().equals(robot)) {
                client.sendToClient(response);
                return;
            }
        }
    }

    private JSONObject executeDump(Robot target){
        JsonEncode encoder;
        String result;

        result = "OK";
        encoder = new JsonEncode(target, result, "dump");
        encoder.setStatus("DEAD");
        encoder.setMessage("DUMP");
        return encoder.response;
    }
}
