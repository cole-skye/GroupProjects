package za.co.wethinkcode.SQLDatabase;

import za.co.wethinkcode.Domain.world.World.AbstractWorld;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlDatabase implements DataBase{

    public static final String IN_MEMORY_DB_URL = "jdbc:sqlite::memory:";

    public static final String DISK_DB_URL = "jdbc:sqlite:";

    private static final String serverWorldName = "ServerWorlds";

    private static final String worldObjectsName = "WorldObjects";

    public static void main( String[] args ) {
        final SqlDatabase databaseConnect = new SqlDatabase();
    }

    public static String dbUrl = DISK_DB_URL+"RobotWorlds.db";

    public SqlDatabase( ) {

        try( final Connection connection = DriverManager.getConnection( dbUrl ) ){
//            System.out.println( " >> Response: Connected to Database" );

        }catch( SQLException e ){
            System.err.println( e.getMessage() );
        }
    }

    @Override
    public void initializeServerWorldTable( Connection connection ) {
        try (final Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE "
                    + serverWorldName +
                    "( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    +" name TEXT NOT NULL,"
                    +" size INTEGER NOT NULL,"
                    +" world_object_reference TEXT NOT NULL"
                    +" )");
            System.out.println(" >> Response: Successfully created "+ serverWorldName +" table!");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void initializeWorldObjectsTable(Connection connection ){
        try ( final Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE "
                    + worldObjectsName +
                    "( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    +" worldName TEXT NOT NULL,"
                    +" position_x NOT NULL,"
                    +" position_y NOT NULL,"
                    +" size INTEGER NOT NULL,"
                    +" type TEXT NOT NULL"
                    +" )");
            System.out.println(" >> Response: Successfully created " + worldObjectsName +" table!");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void insertDataInServerWorld(
            final Connection connection,
            String name,
            String world_size,
            String reference
    ) throws SQLException {
        try( final Statement statement = connection.createStatement() ){
            boolean gotAResultSet = statement.execute(
                    "INSERT INTO "
                            + serverWorldName +"( name, size, world_object_reference ) "+
                            "VALUES ( "+ name +", "+ world_size+", "+reference+" )"
            );
            if ( gotAResultSet ){
                throw new RuntimeException( " >> Response: Unexpectedly got a SQL resultset.");
            }
            else{
                final int updateCount = statement.getUpdateCount();
                if( updateCount == 1 ){
                    System.out.println( " >> Response: 1 row INSERTED into "+ serverWorldName);
                }
                else{
                    throw new RuntimeException( " >> Response: Expected 1 row to be inserted, but got " + updateCount );
                }
            }
        }
    }

    @Override
    public void insertDataInWorldObjects(
            final Connection connection,
            String worldName,
            String position_x,
            String position_y,
            String size,
            String type
    ) throws SQLException {
        try( final Statement statement = connection.createStatement() ){
            boolean gotAResultSet = statement.execute(
                    "INSERT INTO " +
                            worldObjectsName +"( worldName, position_x, position_y, size, type )"+
                            "VALUES ( "+worldName+", "+ position_x +", "+ position_y+", "+size+", "+type+" )"
            );
            if ( gotAResultSet ){
                throw new RuntimeException( " >> Response: Unexpectedly got a SQL resultset.");
            }
            else{
                final int updateCount = statement.getUpdateCount();
                if( updateCount == 1 ){
                    System.out.println( " >> Response: 1 row INSERTED into "+ worldObjectsName);
                }
                else{
                    throw new RuntimeException( " >> Response: Expected 1 row to be inserted, but got " + updateCount );
                }
            }
        }
    }

    @Override
    public List<String> readDataFromSelectedWorld(
            final Connection connection,
            String table,
            String column
    ) throws SQLException{
        List<String> resultList = new ArrayList<>();

        try( final Statement statement = connection.createStatement() ){
            boolean gotAResultSet = statement.execute(
                    "SELECT *"
                            +" FROM "+"\""+table+"\""
            );
            if ( !gotAResultSet ){
                throw new RuntimeException( " >> Response: Expected a SQL resultset, but got an update count instead!" );
            }
            try ( ResultSet results = statement.getResultSet() ){
                while( results.next() ){
                    resultList.add( results.getString(column) );

                }
                System.out.println( " >> Response: This is the table result: " + Arrays.toString( resultList.toArray() ) );
            }
        }
        return resultList;
    }

    @Override
    public List<String> readDataWithConditionFromSelectedWorld(
            final Connection connection,
            String table,
            String column,
            String condition
    ) throws SQLException{
        List<String> resultList = new ArrayList<>();

        try( final Statement statement = connection.createStatement() ){
            boolean gotAResultSet = statement.execute(
                "SELECT *"
                    +" FROM "+"\""+table+"\""
                    +" WHERE " + condition
            );
            if ( !gotAResultSet ){
                throw new RuntimeException( " >> Response: Expected a SQL resulset, but got an update count instead!" );
            }
            try ( ResultSet results = statement.getResultSet() ){
                while( results.next() ){
                    resultList.add( results.getString(column) );

                }
                System.out.println( " >> Response: This is the table result: " + Arrays.toString( resultList.toArray() ) );
            }
        }
        return resultList;
    }

    public void addWorldDB(final Connection connection, AbstractWorld world) throws SQLException {
        try( final Statement statement = connection.createStatement() ){
            boolean gotAResultSet = statement.execute(
                    "INSERT INTO "
                            + serverWorldName +"( name, size, world_object_reference ) "+
                            "VALUES ( "+ world.getWorldName() +", "+ world.getWorldSize()+", "+world.getWorldName()+" )"
            );
            if ( gotAResultSet ){
                throw new RuntimeException( " >> Response: Unexpectedly got a SQL resultset.");
            }
            else{
                final int updateCount = statement.getUpdateCount();
                if( updateCount == 1 ){
                    System.out.println( " >> Response: 1 row INSERTED into "+ serverWorldName);
                }
                else{
                    throw new RuntimeException( " >> Response: Expected 1 row to be inserted, but got " + updateCount );
                }
            }
        }
        try( final Statement statement = connection.createStatement() ){
            boolean gotAResultSet = statement.execute(
                    "INSERT INTO " +
                            worldObjectsName +"( worldName, position, size, type )"+
                            "VALUES ( "+world.getWorldName()+", "+ world.getObstacles() +", "+ world.getObstacles().size() +", "+ "obsatcle" +" )"
            );
            if ( gotAResultSet ){
                throw new RuntimeException( " >> Response: Unexpectedly got a SQL resultset.");
            }
            else{
                final int updateCount = statement.getUpdateCount();
                if( updateCount == 1 ){
                    System.out.println( " >> Response: 1 row INSERTED into "+ worldObjectsName);
                }
                else{
                    throw new RuntimeException( " >> Response: Expected 1 row to be inserted, but got " + updateCount );
                }
            }
        }
    }
}
