package za.co.wethinkcode.Domain.ServerCommands;

import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;
import za.co.wethinkcode.SQLDatabase.SqlDatabase;
import za.co.wethinkcode.SQLDatabase.WorldData;
import za.co.wethinkcode.Socket.SocketServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Prompts user for input regarding world name
 * and checks the name doesn't exist already.
 * Saves world to SQL Database and notifies user
 * that command is complete.
 */
public class SaveWorldCommand {

    static Scanner scanner = new Scanner( System.in );

    public SaveWorldCommand() {
        saveWorld();
    }

    private void saveWorld(){
        List<WorldObject> obstacleList = SocketServer.getWorld().getObstacles();

        SqlDatabase dbInteraction = new SqlDatabase();

        System.out.println( " > What would you like to save your world as? " );
        String name = scanner.nextLine();

        while ( WorldData.contains( name ) ){
            System.out.println(
                    " > Response: This name is taken already...\n"
                            +" > Please provide a different name: " );
            name = scanner.nextLine();
        }

        try( final Connection connection = DriverManager.getConnection( SqlDatabase.dbUrl ) ) {
            for ( WorldObject obstacle : obstacleList ){
                String position_x = Integer.toString( obstacle.getX() );
                String position_y = Integer.toString( obstacle.getY() );

                dbInteraction.insertDataInWorldObjects(
                        connection,
                        "\""+name+"\"",
                        position_x,
                        position_y,
                        "1",
                        "\"OBSTACLE\"");
            }

            dbInteraction.insertDataInServerWorld(
                    connection,
                    "\""+name+"\"",
                    Integer.toString( SocketServer.getWorld().getWorldSize() ),
                    "\""+name+"\""
            );
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
        System.out.println(" > Response: Command Completed");
    }
}
