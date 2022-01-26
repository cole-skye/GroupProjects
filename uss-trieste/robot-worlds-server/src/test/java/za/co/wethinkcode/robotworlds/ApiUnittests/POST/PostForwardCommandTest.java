package za.co.wethinkcode.robotworlds.ApiUnittests.POST;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.WebAPI.ApiServer;

import static org.junit.jupiter.api.Assertions.*;

public class PostForwardCommandTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(5003);
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    @DisplayName("POST /command FORWARD")
    void shouldDoForwardCommand() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + 5003 + "/robot/test1")
                .header("Content-Type", "application/json")
                .body("")
                .asJson();
        assertEquals(201, response.getStatus());

        response = Unirest.get("http://localhost:" + 5003 + "/world").asJson();
        kong.unirest.json.JSONArray robotsResponse = (kong.unirest.json.JSONArray) response.getBody().getObject().get("robots");

        assertEquals(200, response.getStatus());
        assertTrue( robotsResponse.length() > 0);

        response = Unirest.post("http://localhost:" + 5003 + "/command")
                .header("Content-Type", "application/json")
                .body("{\"robot\":\"test1\", \"command\":\"forward\", \"arguments\":[\"1\"]}")
                .asJson();
        assertEquals(200, response.getStatus());
    }
}