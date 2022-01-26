package JsonUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import Robot.Robot;

public class JsonEncode {
    public JSONObject response;

    public JsonEncode(Robot target, String result, String command) {
        response = generateResponse(target, result, command);
    }

    /**
     * Adds all the items of the given Integer array to a JSONArray object and returns
     * this JSONArray object.
     *
     * @param arr   the Integer array to convert into a JSONArray object.
     * @return the JSONArray object containing the given array values.
     */
    private static JSONArray integerArrToJson(int[] arr) {
        JSONArray jsonArr;

        jsonArr = new JSONArray();
        for (Object value: arr) {
            jsonArr.add(value);
        }
        return jsonArr;
    }

    /**
     * Generates a JSONObject with the given IRobot object's Class Type, name
     * and the given instruction. This JSONObject object is returned.
     *
     * @param target       IRobot object used to fill JSONObject.
     * @param instruction  String value of a command and argument.
     * @return JSONObject object with the given IRobot object's Class Type, name and command.
     */
    private static JSONObject generateKeys(Robot target, String instruction) {
        JSONObject dictKeys = new JSONObject();

        dictKeys.put("name", target.getName());
        dictKeys.put("command", instruction);
        dictKeys.put("message", "");
        return dictKeys;
    }

    /**
     * Retrieves String value of the given Direction eNum.
     *
     * @param direction Direction object to convert to a String value.
     * @return String value of the Direction object.
     */
    private static String directionToString(Robot.Direction direction) {
        switch(direction) {
            case NORTH:
                return "NORTH";
            case SOUTH:
                return "SOUTH";
            case WEST:
                return "WEST";
        }
        return "EAST";
    }

    /**
     * Retrieves String value of the given Status eNum.
     *
     * @param status Status eNum to convert to a String value.
     * @return the String value of the given Status eNum.
     */
    private static String statusToString(Robot.Status status) {
        switch(status) {
            case NORMAL:
                return "NORMAL";
            case DEAD:
                return "DEAD";
            case SETMINE:
                return "SETMINE";
            case RELOADING:
                return "RELOADING";
        }
        return "REPAIRING";
    }

    /**
     * Creates the response JSONObject's 'state'-key's JSONObject value pair.
     *
     * @param target IRobot object used to populate the value's JSONObject object.
     * @return the 'state'-key's JSONObject value pair.
     */
    private static JSONObject generateState(Robot target){
        JSONObject state = new JSONObject();
        int[] coordinates;

        coordinates = new int[]{target.getPosition().getX(), target.getPosition().getY()};
        state.put("position", integerArrToJson(coordinates));
        state.put("direction", directionToString(target.getDirection()));
        state.put("shields", target.getShieldStrength());
        state.put("shots", target.getShotsLeft());
        state.put("status", statusToString(target.getStatus()));
        return state;
    }

    /**
     * Populates the response JSONObject by using information from the given IRobot object,
     * the String 'result' and the String 'command'.
     *
     * @param target   IRobot object used to populate the JSONObject object.
     * @param result   The result of the command.
     *                 OK    :  Command successfully processed by the world.
     *                 ERROR :  Command unsuccessfully processed by the world.
     * @param command  Requested Command that was executed successfully, or unsuccessfully.
     * @return the JSONObject response.
     */
    public static JSONObject generateResponse(Robot target, String result, String command) {
        JSONObject response = new JSONObject();

        response.put("result", result);
        response.put("data", generateKeys(target, command));
        if (result.equals("OK")) {
            response.put("state", generateState(target));
        }
        return response;
    }

    public void setStatus(String status) {
        ((JSONObject)response.get("state")).put("status", status);
    }

    public void setMessage(String message) {
        ((JSONObject)response.get("data")).put("message", message);
    }

    public JSONObject createFireCommandResponse(boolean hit, int shotRobotDistance, String shotRobotName, JSONObject shotRobotState, int shotsLeft) {
        JSONObject data;
        JSONObject state;

        data = new JSONObject();
        if (!hit) {
            data.put("message", "Miss");
        } else {
            state = new JSONObject();
            data.put("message", "Hit");
            data.put("distance", shotRobotDistance);
            data.put("name", shotRobotName);
            data.put("state", shotRobotState);
            state.put("shots", shotsLeft);
            response.put("state", state);
        }
        response.put("data", data);
        return response;
    }

    public JSONObject getState() {
        return (JSONObject) response.get("state");
    }

}
