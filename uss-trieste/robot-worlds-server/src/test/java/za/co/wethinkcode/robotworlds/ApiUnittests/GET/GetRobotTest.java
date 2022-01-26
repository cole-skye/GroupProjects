package za.co.wethinkcode.robotworlds.ApiUnittests.GET;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.WebAPI.ApiServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetRobotTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(5021);
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    @DisplayName("GET /robot")
    void shouldCreateAndReturnRobotList() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + 5021 + "/robot/test9")
                .header("Content-Type", "application/json")
                .body("")
                .asJson();
        assertEquals(201, response.getStatus());


        response = Unirest.get("http://localhost:" + 5021 + "/robot").asJson();
        assertTrue(response.getBody().toString().contains("test9"));
    }
}
