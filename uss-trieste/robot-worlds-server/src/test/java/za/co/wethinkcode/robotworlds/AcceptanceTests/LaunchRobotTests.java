package za.co.wethinkcode.robotworlds.AcceptanceTests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.CurriculumClient.RobotWorldClient;
import za.co.wethinkcode.CurriculumClient.RobotWorldJsonClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * As a player
 * I want to launch my robot in the online robot world
 * So that I can break the record for the most robot kills
 */
class LaunchRobotTests {
    private final static int DEFAULT_PORT = 7000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    private final RobotWorldClient serverClient_2 = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
        serverClient_2.connect(DEFAULT_IP,DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
        serverClient_2.disconnect();
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

        // And the position should be (x:0, y:0)
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertEquals(0, response.get("data").get("position").get(0).asInt());
        assertEquals(0, response.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }
    @Test
    public void invalidLaunchShouldFail(){
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a invalid launch request with the command "luanch" instead of "launch"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"luanch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And the message "Unsupported command"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Unsupported command"));
    }
    @Test
    public void worldNotFullSucceds(){
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I launch a robot it will successfully create a robot
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an ok response
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());
    }


    @Test
    public void worldFullFails(){
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I launch a robot it will successfully create a robot
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an ok response
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        int i = 0;
        while (!response.get("result").asText().equals("ERROR")) {

            // When I launch a robot it will unsuccessfully create a robot
            request = "{" +
                    "\"robot\": \"SAL" + i + "\"," +
                    "\"command\": \"launch\"," +
                    "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                    "}";
            response = serverClient.sendRequest(request);
//            System.out.println(response);
            // Then I should get an error response
            assertNotNull(response.get("result"));
            i++;
        }
//        assertEquals("ERROR", response.get("result").asText());
        // When I launch a robot it will not unsuccessfully create a robot
//        request = "{" +
//                "\"robot\": \"KAL\"," +
//                "\"command\": \"launch\"," +
//                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
//                "}";
//        response = serverClient.sendRequest(request);
//        System.out.println(response);
        // Then I should get an error response
//        assertNotNull(response.get("result"));
        assertEquals("No more space in this world", response.get("data").get("message").asText());
    }

    @Test
    public void nameTakenFails() {
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());
        // When I launch a robot it will successfully create a robot
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an ok response
        assertNotNull(response.get("result"));
        assertEquals("OK", response.get("result").asText());

        // When I launch a second robot with the same name it should fail
        request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"launch\"," +
                "\"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        response = serverClient.sendRequest(request);

        // Then I should get an error response
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());
    }

    @Test
    public void validLaunchTwoRobotsShouldSucceed(){

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

        assertNotNull(response_forward.get("result"));
        assertEquals("OK", response_forward.get("result").asText());

        // client 2
        assertTrue(serverClient_2.isConnected());

        String request_robot_2 = "{" +
                "  \"robot\": \"Bob\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"shooter\",\"5\",\"5\"]" +
                "}";
        JsonNode response_robot_2 = serverClient_2.sendRequest(request_robot_2);

        // Then I should get a valid response from the server
        assertNotNull(response_robot_2.get("result"));
        assertEquals("OK", response_robot_2.get("result").asText());

        String request_back = "{" +
                "  \"robot\": \"Bob\"," +
                "  \"command\": \"forward\"," +
                "  \"arguments\": [\"1\"]" +
                "}";
        JsonNode response_back = serverClient.sendRequest(request_back);

        assertNotNull(response_back.get("result"));
        assertEquals("OK", response_back.get("result").asText());

    }
}
