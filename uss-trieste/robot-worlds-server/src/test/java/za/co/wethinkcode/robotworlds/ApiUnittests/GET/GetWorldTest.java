package za.co.wethinkcode.robotworlds.ApiUnittests.GET;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.WebAPI.ApiServer;

import static org.junit.jupiter.api.Assertions.*;

public class GetWorldTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(4999);
    }

    @AfterEach
    void stop() {
        server.stop();
    }



    @Test
    @DisplayName("GET /world")
    public void shouldGetCurrentWorld() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:" + 4999 + "/world").asJson();
        assertTrue(response.isSuccess());
        assertTrue(response.getBody().getObject().has("size"));
        assertTrue(response.getBody().getObject().has("obstacles"));

    }
}
