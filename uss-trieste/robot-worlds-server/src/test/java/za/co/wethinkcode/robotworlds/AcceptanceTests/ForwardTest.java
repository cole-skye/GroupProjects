package za.co.wethinkcode.robotworlds.AcceptanceTests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.CurriculumClient.RobotWorldClient;
import za.co.wethinkcode.CurriculumClient.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

public class ForwardTest {

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
    public void validLaunchShouldSucceed(){
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        String request_forward = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"forward\"," +
                "  \"arguments\": [\"1\"]" +
                "}";
        JsonNode response_forward = serverClient.sendRequest(request_forward);
        System.out.println(response_forward.get("data"));

        assertNotNull(response_forward.get("result"));
        assertEquals("OK", response_forward.get("result").asText());

        // And the position should be (x:0, y:0)
        assertNotNull(response_forward.get("data"));
        assertNotNull(response_forward.get("data").get("position"));
        assertEquals(0, response_forward.get("data").get("position").get(0).asInt());
        assertEquals(1, response_forward.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }
    @Test
    public void ForwardPastWorldLimitShouldFail(){
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        String request_forward = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"forward\"," +
                "  \"arguments\": [\"201\"]" +
                "}";
        JsonNode response_forward = serverClient.sendRequest(request_forward);
        System.out.println(response_forward.get("data"));

        assertNotNull(response_forward.get("result"));
        assertEquals("OK", response_forward.get("result").asText());

        // And the position should be (x:0, y:0)
        assertNotNull(response_forward.get("data"));

        assertTrue(response_forward.get("data").get("message").asText().contains("At the NORTH edge"));

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }
}
