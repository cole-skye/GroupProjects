package za.co.wethinkcode.robotworlds.ApiUnittests.POST;

import com.google.gson.JsonArray;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.WebAPI.ApiServer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostLeftCommandTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(5005);
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    @DisplayName("POST /command Turn Left")
    void shouldDoTurnLeftCommand() {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:" + 5005 + "/robot/test3")
                .header("Content-Type", "application/json")
                .body("")
                .asJson();
        assertEquals(201, response.getStatus());

        response = Unirest.get("http://localhost:" + 5005 + "/world").asJson();
        kong.unirest.json.JSONArray robotsResponse = (kong.unirest.json.JSONArray) response.getBody().getObject().get("robots");

        assertEquals(200, response.getStatus());
        assertTrue( robotsResponse.length() > 0);

        response = Unirest.post("http://localhost:" + 5005 + "/command")
                .header("Content-Type", "application/json")
                .body("{\"robot\":\"test3\", \"command\":\"turn\", \"arguments\":[\"left\"]}")
                .asJson();
        assertEquals(200, response.getStatus());

    }
}
