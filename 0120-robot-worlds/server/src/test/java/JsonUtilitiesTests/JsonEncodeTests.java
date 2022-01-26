package JsonUtilitiesTests;

import JsonUtils.JsonEncode;
import Robot.Robot;
import org.json.simple.JSONObject;
import org.junit.Test;
import Robot.Position;

import static org.junit.Assert.*;

public class JsonEncodeTests {

    @Test
    public void OK() {
        JSONObject response;

        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        response = JsonEncode.generateResponse(robot, "OK", "forward 10");
        assertEquals(response.get("result"), "OK");
    }

    @Test
    public void setStatusTest() {
        JsonEncode encoder;
        Robot robot;
        JSONObject state;

        robot = new Robot("CrashTestDummy", "normal", 0, 0);
        encoder = new JsonEncode(robot, "OK", "forward");
        encoder.setStatus("DEAD");
        state = ((JSONObject)encoder.response.get("state"));
        assertEquals(state.get("status"), "DEAD");
    }

    @Test
    public void setMessageTest() {
        JsonEncode encoder;
        Robot robot;
        JSONObject data;

        robot = new Robot("CrashTestDummy", "normal", 0, 0);
        encoder = new JsonEncode(robot, "OK", "forward");
        encoder.setMessage("Obstructed");
        data = ((JSONObject)encoder.response.get("data"));
        assertEquals(data.get("message"), "Obstructed");
    }

    @Test
    public void shotRobotResponseTest() {
        JsonEncode encoderA;
        JsonEncode encoderB;
        Robot robotA;
        Robot robotB;

        robotA = new Robot("CrashTestDummyA", "normal", 0,4);
        robotB = new Robot("CrashTestDummyB", "sniper", 0,0);
        encoderA = new JsonEncode(robotA, "OK", "fire");
        encoderB = new JsonEncode(robotB, "OK", "fire");
        encoderA.createFireCommandResponse(true, 5, robotB.getName(), encoderB.getState(), robotA.getShotsLeft());
        assertEquals("{\"result\":\"OK\",\"data\":{\"distance\":5,\"name\":\"CrashTestDummyB\",\"state\":{\"shields\":0,\"position\":[0,0],\"shots\":0,\"direction\":\"NORTH\",\"status\":\"NORMAL\"},\"message\":\"Hit\"},\"state\":{\"shots\":4}}", encoderA.response.toString());
    }

    @Test
    public void missedShotResponseTest() {
        JsonEncode encoderA;
        JsonEncode encoderB;
        Robot robotA;
        Robot robotB;

        robotA = new Robot("CrashTestDummyA", "normal", 0,4);
        Position position = new Position(0,0);
        encoderA = new JsonEncode(robotA, "OK", "fire");
        encoderA.createFireCommandResponse(false, 0, "", new JSONObject(), robotA.getShotsLeft());
        System.out.println(encoderA.response);
        assertEquals("{\"result\":\"OK\",\"data\":{\"message\":\"Miss\"},\"state\":{\"shields\":0,\"position\":[0,0],\"shots\":4,\"direction\":\"NORTH\",\"status\":\"NORMAL\"}}", encoderA.response.toString());
    }


}
