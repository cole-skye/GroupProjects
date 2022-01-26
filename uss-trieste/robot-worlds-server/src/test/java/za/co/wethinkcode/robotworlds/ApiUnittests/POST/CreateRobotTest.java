package za.co.wethinkcode.robotworlds.ApiUnittests.POST;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.WebAPI.ApiServer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateRobotTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(5010);
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    @DisplayName("POST /robot")
    void shouldCreateRobot() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + 5010 + "/robot/test6")
                .header("Content-Type", "application/json")
                .body("")
                .asJson();
        assertEquals(201, response.getStatus());
    }
}
