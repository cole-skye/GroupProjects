package Commands.RobotCommands;

import Commands.RobotCommand;
import JsonUtils.JsonEncode;
import Robot.Robot;
import World.World;
import org.json.simple.JSONObject;

public class BackCommand extends RobotCommand {

    public BackCommand(String argument) {
        super("back", argument);
    }

    @Override
    public JSONObject execute(Robot target, World world) {
        int nrSteps;
        World.UpdateResponse response;

        nrSteps = Integer.parseInt(getArgument());
        response = world.updatePosition(target, -nrSteps);
        return createBackResponse(target, response);
    }

    private JSONObject createBackResponse(Robot target, World.UpdateResponse updateResponse) {
        JsonEncode encoder;
        String result;
        JSONObject state;
        JSONObject response;

        result = "OK";
        encoder = new JsonEncode(target, result, "back");
        response = new JSONObject();
        switch (updateResponse) {
            case SUCCESS:
                encoder.setMessage("Done");
                break;
            case FAILED_OBSTRUCTED_PITFALL:
                encoder.setStatus("DEAD");
                encoder.setMessage("Fell");
                break;
            case FAILED_OBSTRUCTED_MINE:
                state = encoder.getState();
                response.put("state", state);
                return (response);
            case FAILED_OBSTRUCTED_OBSTACLE:
            case FAILED_OUTSIDE_WORLD:
            case FAILED_OBSTRUCTED_ROBOT:
                encoder.setMessage("Obstructed");
        }
        response = encoder.response;
        return response;
    }
}
