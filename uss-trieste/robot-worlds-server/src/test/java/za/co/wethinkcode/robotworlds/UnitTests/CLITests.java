package za.co.wethinkcode.robotworlds.UnitTests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.Domain.Map.EmptyMap;
import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.World.World;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CLITests {

    @Test
    void testPortDefault(){
        SocketServer.setWorld(new World(new EmptyMap()));
        Integer port = SocketServer.configReader.getPORT();
        assertEquals(7000, port);
    }

}
