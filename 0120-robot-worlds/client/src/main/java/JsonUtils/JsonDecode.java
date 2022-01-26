package JsonUtils;

import Robot.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonDecode {
    public JSONObject response;
    public JSONObject data;
    public JSONObject state;

    public JsonDecode(JSONObject response, IRobot robot) {
        this.response = response;
        this.data = getDataJsonObject();
        this.state = getStateJsonObject();
        updateRobot(robot);
    }

    /**
     * Retrieves the JSONObject-value of the data-key in the Response JSONObject.
     *
     * @return the JSONObject value of the data-key.
     */
    private JSONObject getDataJsonObject() {
        JSONObject data;

        data = (JSONObject) response.get("data");
        return data;
    }

    /**
     * Retrieves the JSONObject-value of the state-key in the Response JSONObject.
     *
     * @return the JSONObject value of the state-key.
     */
    private JSONObject getStateJsonObject() {
        JSONObject state;

        state = (JSONObject) response.get("state");
        return state;
    }

    /**
     * Updates the IRobot object with the values retrieved from the JSONObject Response.
     *
     * @param robot the robot who performed the command that the server responded on.
     */
    private void updateRobot(IRobot robot) {
        if (successful()) {
            robot.setPosition(getPosition());
            robot.setDirection(getDirection());
            robot.setShieldStrength(getShieldStrength());
            robot.setMaxShots(getGunShotsLeft());
        }
    }

    /**
     * Retrieves the value paired with the position-key in the JSONObject Response. This
     * information represents the robot's x-, and y-values in the server program's 'world'.
     * These values are used to create a new Position object, which is then returned.
     *
     * @return the IRobot object's Position object in the server program's 'world' simulation.
     */
    public Position getPosition() {
        JSONArray position;
        int x;
        int y;

        position = (JSONArray) state.get("position");
        x = Integer.parseInt(position.get(0).toString());
        y = Integer.parseInt(position.get(1).toString());
        return new Position(x, y);
    }

    /**
     * Retrieves the value paired with the direction-key in the JSONObject Response. This
     * information represents direction (NORTH, SOUTH, WEST, or EAST) that the robot is facing
     * in the server program's 'world' simulation. This value is used to determine which
     * Direction enum to return.
     *
     * @return the Direction enum corresponding with the direction the robot is facing in the
     *         server program's 'world'-simulation.
     */
    public IRobot.Direction getDirection() {
        String strDirection;

        strDirection = (String) state.get("direction");
        switch (strDirection.toLowerCase()) {
            case "north":
                return IRobot.Direction.NORTH;
            case "south":
                return IRobot.Direction.SOUTH;
            case "east":
                return IRobot.Direction.EAST;
        }
        return IRobot.Direction.WEST;
    }

    /**
     * Retrieves the value paired with the shields-key in the JSONObject Response. This
     * information represents the IRobot object's shield strength in the server program's
     * 'world'-simulation.
     *
     * @return the shield strength attribute of the robot in the server programs 'world'-simulation..
     */
    public int getShieldStrength() {
        return Integer.parseInt(state.get("shields").toString());
    }

    /**
     * Retrieves the value paired with the shots-key in the JSONObject Response. This
     * information represents the IRobot object's total amount of gun shots in the server
     * program's 'world'-simulation.
     *
     * @return the leftover gun shots of the robot in the server programs 'world'-simulation.
     */
    public int getGunShotsLeft() {
        return Integer.parseInt(state.get("shots").toString());
    }

    /**
     * Checks whether the command that was sent to the server program to execute, was successfully
     * executed, by confirming that the paired value of the result-key in the JSONObject Response
     * is the String "OK".
     *
     * @return true if the command was successfully executed.
     */
    public boolean successful() {
        Object result;

        result = response.get("result");
        if (result == null) return true;
        return (((String)result).equals("OK"));
    }

    public void setRobotStatus(IRobot robot, String command, String[] arguments) {
        String status;

        status = "";
        if (state.get("status").equals("DEAD")) {
            robot.setStatus("I died. Bye bye.");
            System.out.println(robot.getStatus());
            System.exit(1);
        }
        if (command.equals("forward") || command.equals("back")) {
            status = getMoveCommandStatus(command, arguments[0]);
        } else if (command.equals("right") || command.equals("left")) {
            status = getTurnCommandStatus(robot, command);
        } else if (command.equals("look")) {
            status = getLookCommandStatus();
        } else if(command.equals("mine")) {
            status = getMineCommandStatus();
        } else { //launch command
            status = getLaunchCommandStatus();
        }
        robot.setStatus(status);
    }

    private String getMineCommandStatus(){
        String status;

        status = "Mine has been activated.";
        return status;
    }

    private String getLookCommandStatus() {
        String status;
        JSONArray objects;
        JSONObject object;

        objects = (JSONArray) data.get("objects");
        if (objects == null) {
            return "I do not see any obstructions in the way.";
        }
        status = (objects.size() == 0) ? "I do not see any obstructions in the way." : "I see the following obstructions:\n";
        for (int i = 0; i < objects.size(); i++) {
            object = (JSONObject) objects.get(i);
            status += "- "+object.get("direction")+" : "+((String)object.get("type")).toLowerCase()+" is "+object.get("distance")+" steps away.";
            if (i != objects.size()-1) {
                status += "\n";
            }
        }
        return status;
    }

    private String getMoveCommandStatus(String command, String arguments) {
        //TODO: make kill robot
        String status;

        status = "Oh no... I got killed!";
        if (((String)data.get("message")).equals("Done")) {
            status = "Moved "+command+" by "+arguments+" steps.";
        } else if (((String)data.get("message")).equals("Obstructed")) {
            status = "There is an obstacle in the way.";
        }
        return status;
    }

    private String getTurnCommandStatus(IRobot robot, String turn) {
        String status;

        status = "Turned "+turn+". I am now facing "+robot.getDirection()+".";
        return status;
    }

    private String getLaunchCommandStatus() {
        return "Launched into the new world!";
    }

}
