package za.co.wethinkcode.robotworlds.AcceptanceTests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.CurriculumClient.RobotWorldClient;
import za.co.wethinkcode.CurriculumClient.RobotWorldJsonClient;

import java.util.ArrayList;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class LookTest {

    private final static int DEFAULT_PORT = 7000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();
    private final RobotWorldClient serverClient_2 = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer() {
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClient_2.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer() {
        serverClient.disconnect();
        serverClient_2.disconnect();
    }

    @Test
    public void validLookShouldSucceed() {
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request_launch = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response_launch = serverClient.sendRequest(request_launch);

        // Then I should get a valid response from the server
        assertNotNull(response_launch.get("result"));
        assertEquals("OK", response_launch.get("result").asText());

        // When I send a valid look request to the server
        String request_look = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"look\"," +
                "  \"arguments\": []" +
                "}";
        JsonNode response_look = serverClient.sendRequest(request_look);

        //Displays the data from the "look" command
//        System.out.println(response_look.get("data"));

        // Then I should get a valid response from the server
        assertNotNull(response_look.get("result"));
        assertEquals("OK", response_look.get("result").asText());


        assertNotNull(response_look.get("data"));
        assertFalse(response_look.get("data").asText().contains("objects"));

        // Visibility is 1 and I check to see the "EDGE" in all directions
        assertFalse(response_look.get("data").get("objects").asText().contains("Obstacles"));

        // And I should also get the state of the robot
        assertNotNull(response_look.get("state"));


    }
}
