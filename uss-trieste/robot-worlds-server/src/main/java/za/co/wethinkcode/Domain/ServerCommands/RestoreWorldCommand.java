package za.co.wethinkcode.Domain.ServerCommands;

import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;
import za.co.wethinkcode.SQLDatabase.WorldData;
import za.co.wethinkcode.Socket.SocketServer;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Restores previously saved worlds from SQL Database.
 */
public class RestoreWorldCommand {
    static Scanner scanner = new Scanner( System.in );

    public RestoreWorldCommand(){
        restore();
    }

    public void restore() {

        List<String> worldsList = WorldData.read("ServerWorlds", "name" );

        if ( worldsList.isEmpty() ){
            System.out.println( " > Response: The database does not contain any saved worlds.\n" +
                    "Use the 'save' command to add more worlds the database." );
        }else {
            System.out.println( " > Response: You may restore the displayed available worlds: "+ Arrays.toString(worldsList.toArray()) );
            String worldSelection = scanner.nextLine();
            while ( worldSelection.isBlank() ){
                if ( worldSelection.equals("cancel") ){
                    System.out.println(" > Cancelled restore process");
                    break;
                }
                System.out.println( " > Response: You may restore the displayed available worlds: "+ Arrays.toString(worldsList.toArray()) );
                System.out.println( " > Please input one of the available worlds: " );
                worldSelection = scanner.nextLine();

            }
            List<WorldObject> obstacles = WorldData.getObstaclesFromDB( worldSelection );

            int worldSize = WorldData.getWorldSizeFromDB( worldSelection );

            SocketServer.getWorld().setObstacles(obstacles);

            SocketServer.getWorld().setWorldSize(worldSize);

            System.out.println(Arrays.toString(obstacles.toArray()));

            System.out.println(" > Current World Size: " + worldSize);

        }
    }
}
