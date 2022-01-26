package za.co.wethinkcode.robotworlds.ApiUnittests.POST;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.WebAPI.ApiServer;

import static org.junit.jupiter.api.Assertions.*;

public class CreateObstacleTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(5011);
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    @DisplayName("POST /obstacle")
    void shouldCreateObstacle() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + 5011 + "/obstacle")
                .header("Content-Type", "application/json")
                .body("{\"x\":\"1\", \"y\":\"1\"}")
                .asJson();
        assertEquals(201, response.getStatus());

        assertFalse(ApiServer.world.getObstacles().isEmpty());

    }
}
