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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostRightCommandTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(5006);
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    @DisplayName("POST /command Turn Right")
    void shouldDoCommand() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + 5006 + "/robot/test")
                .header("Content-Type", "application/json")
                .body("")
                .asJson();
        assertEquals(201, response.getStatus());

        response = Unirest.get("http://localhost:" + 5006 + "/world").asJson();
        assertEquals(200, response.getStatus());
        kong.unirest.json.JSONArray robotsResponse = (kong.unirest.json.JSONArray) response.getBody().getObject().get("robots");

        assertEquals(200, response.getStatus());
        assertTrue(robotsResponse.length() > 0);

        response = Unirest.post("http://localhost:" + 5006 + "/command")
                .header("Content-Type", "application/json")
                .body("{\"robot\":\"test\", \"command\":\"turn\", \"arguments\":[\"right\"]}")
                .asJson();
        assertEquals(200, response.getStatus());
    }
}
