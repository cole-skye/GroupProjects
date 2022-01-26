package za.co.wethinkcode.SQLDatabase;

import za.co.wethinkcode.Domain.world.World.AbstractWorld;
import za.co.wethinkcode.Domain.world.WorldObjects.Obstacle.SquareObstacle;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldData {

    public static HashMap worldObjects = new HashMap();

    private static List<String> results = new ArrayList<>();

    public static List<String> read( String table, String column ){
        SqlDatabase dbInteraction = new SqlDatabase();

        try( final Connection connection = DriverManager.getConnection( SqlDatabase.dbUrl ) ) {

            results = dbInteraction.readDataFromSelectedWorld( connection , table, column );
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }

        return results;
    }

    public static void createServerWorldsTable(){
        DataBase dbInteraction = new SqlDatabase();

        try( final Connection connection = DriverManager.getConnection( SqlDatabase.dbUrl ) ){
            dbInteraction.initializeServerWorldTable( connection );
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void createWorldObjectsTable(){
        DataBase dbInteraction = new SqlDatabase();

        try( final Connection connection = DriverManager.getConnection( SqlDatabase.dbUrl ) ){
            dbInteraction.initializeWorldObjectsTable( connection );
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static List readWithCondition(String table, String column, String condition){
        SqlDatabase dbInteraction = new SqlDatabase();

        try( final Connection connection = DriverManager.getConnection( SqlDatabase.dbUrl ) ) {
            results = dbInteraction.readDataWithConditionFromSelectedWorld( connection , table, column, condition );
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }

        return results;
    }


    public static boolean contains( String worldName ){
        results = read( "ServerWorlds", "name");

        return results.contains(worldName);
    }

    public static List<WorldObject> getObstaclesFromDB( String worldName ){

        List<WorldObject> obstacles = new ArrayList<>();

        List<String> obstacle_x_list = readWithCondition(
                "WorldObjects",
                "position_x",
                "WorldObjects.worldName = \""+worldName+"\"");

        List<String> obstacle_y_list = readWithCondition(
                "WorldObjects",
                "position_y",
                "WorldObjects.worldName = \""+worldName+"\"");

        for (int i = 0; i < obstacle_x_list.size(); i++) {

            WorldObject obstacle = new SquareObstacle(Integer.parseInt(obstacle_x_list.get(i))
                , Integer.parseInt(obstacle_y_list.get(i)));
            obstacles.add(obstacle);
        }

        worldObjects.put("obstacles", obstacles);


        return obstacles;

    }

    public static int getWorldSizeFromDB( String worldName ){

        int worldSize = 0;

        List<String> worldSizes = readWithCondition(
                "ServerWorlds",
                "size",
                "ServerWorlds.name = \""+worldName+"\"");

        for (int i = 0; i < worldSizes.size(); i++) {
            worldSize = Integer.parseInt(worldSizes.get(i));
        }

        worldObjects.put("size", worldSize);

        return worldSize;

    }

    public static int getWorldId( String worldName ){

        int worldSize = 0;

        List<String> worldSizes = readWithCondition(
                "ServerWorlds",
                "id",
                "ServerWorlds.name = \""+worldName+"\"");

        for (int i = 0; i < worldSizes.size(); i++) {
            worldSize = Integer.parseInt(worldSizes.get(i));
        }

        worldObjects.put("size", worldSize);

        return worldSize;

    }

    public static void putWorldDB(AbstractWorld world) {
        SqlDatabase dbInteraction = new SqlDatabase();

        try( final Connection connection = DriverManager.getConnection( SqlDatabase.dbUrl ) ) {
            dbInteraction.addWorldDB(connection, world);
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
