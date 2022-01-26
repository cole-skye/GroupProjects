package za.co.wethinkcode.SQLDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DataBase {

    void initializeServerWorldTable( Connection connection );

    void initializeWorldObjectsTable( Connection connection );

    void insertDataInServerWorld( final Connection connection, String name, String world_size, String reference ) throws SQLException;

    void insertDataInWorldObjects( final Connection connection, String worldName, String position_x, String position_y, String size, String type ) throws SQLException;

    List<String> readDataFromSelectedWorld( final Connection connection, String table, String column ) throws SQLException;

    List<String> readDataWithConditionFromSelectedWorld( final Connection connection, String table, String column, String condition ) throws SQLException;

}
