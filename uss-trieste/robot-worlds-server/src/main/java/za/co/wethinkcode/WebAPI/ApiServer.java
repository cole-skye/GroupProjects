package za.co.wethinkcode.WebAPI;

import io.javalin.Javalin;
import za.co.wethinkcode.Domain.Map.TestMap;
import za.co.wethinkcode.Domain.world.World.AbstractWorld;
import za.co.wethinkcode.Domain.world.World.World;

/**
 * A Http SocketServer that supports robots and robot commands and is configured to application/json.
 * This SocketServer works hand-in-hand with the Robot Worlds mobile app.
 */
public class ApiServer {
    public static AbstractWorld world = new World(new TestMap());

    private static int PORT = 8080;

    private final Javalin server;

    public ApiServer() {
        server = Javalin.create(config -> {
            config.defaultContentType = "application/json";
        });


        this.server.get("/world/{name}", context -> ApiHandler.getOne(context));
        this.server.get("/robot", context -> ApiHandler.getAllRobots(context));
        this.server.post("/robot/{name}", context -> ApiHandler.create(context));
        this.server.post("/command", context -> ApiHandler.doCommand(context));
        this.server.post("/obstacle", context -> ApiHandler.addObstacles(context));
        this.server.delete("/obstacle", context -> ApiHandler.deleteObstacles(context));
        this.server.delete("/robot/{name}", context -> ApiHandler.deleteRobot(context));

//        this.server.post("/world",context -> ApiHandler.saveWorld(context));
        this.server.get("/world", context -> ApiHandler.getAll(context));
        this.server.get("/loadWorld", context -> ApiHandler.loadWorld(context));

    }

    public static void main(String[] args) {
        ApiServer server = new ApiServer();
        server.start(PORT);
    }

    public void setPORT(int PORT) {
        ApiServer.PORT = PORT;
    }

    public void start(int port) {
        this.server.start(port);
    }

    public void stop() {
        this.server.stop();
    }

}
