package za.co.wethinkcode.robotworlds.ApiUnittests.DELETE;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.WebAPI.ApiServer;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteObstacleTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(5020);
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    @DisplayName("DELETE /obstacle")
    void shouldCreateAndDeleteObstacle() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + 5020 + "/obstacle")
                .header("Content-Type", "application/json")
                .body("{\"x\":\"1\", \"y\":\"1\"}")
                .asJson();
        assertEquals(201, response.getStatus());

        assertFalse(ApiServer.world.getObstacles().isEmpty());

        response = Unirest.delete("http://localhost:" + 5020 + "/obstacle").asJson();

        assertTrue(ApiServer.world.getObstacles().isEmpty());

    }
}
