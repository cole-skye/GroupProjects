package za.co.wethinkcode.robotworlds.ApiUnittests.GET;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.*;
import za.co.wethinkcode.WebAPI.ApiServer;

import static org.junit.jupiter.api.Assertions.*;

public class GetSpecificWorldTest {
    ApiServer server;

    @BeforeEach
    void start() {
        server = new ApiServer();
        server.start(5000);
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    @Test
    @DisplayName("GET /world/{world}")
    public void shouldGetSpecifiedWorld() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:" + 5000 + "/world/test").asJson();
        assertTrue(response.isSuccess());
        assertTrue(response.getBody().getObject().has("size"));
        assertTrue(response.getBody().getObject().has("obstacles"));

    }
}