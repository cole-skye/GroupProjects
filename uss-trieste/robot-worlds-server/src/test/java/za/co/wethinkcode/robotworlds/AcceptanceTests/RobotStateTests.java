package za.co.wethinkcode.robotworlds.AcceptanceTests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.CurriculumClient.RobotWorldClient;
import za.co.wethinkcode.CurriculumClient.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

public class RobotStateTests {
    private final static int DEFAULT_PORT = 7000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }

    @Test
    public void TheRobotExistsInTheWorld(){
//      Given that I have connected to the server
//      And I did launch my robot
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

//      And the server sends a valid response
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

//      When I arrive in the world
//      Then the position should be (x:0, y:0)
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertEquals(0, response.get("data").get("position").get(0).asInt());
        assertEquals(0, response.get("data").get("position").get(1).asInt());

//      And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }

    @Test
    public void TheRobotIsNotInTheWorld(){
//      Given that I have connected to the server
//      And I did not launch my robot
//      When I try to connect run a command

        String request = "{" +
                "  \"robot\": \"\"," +
                "  \"command\": \"forward\"," +
                "  \"arguments\": [\"1\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

//      Then I should get an error response
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

//      And the message "Robot does not exist"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Robot does not exist"));

//      And the state should be null
        assertNull(response.get("state"));

    }

}
