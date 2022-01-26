package za.co.wethinkcode.Domain.ServerCommands;

import za.co.wethinkcode.SQLDatabase.WorldData;

import java.io.File;
import java.util.Arrays;

/**
 * Checks to see if a table exists otherwise creates SQL Table.
 */
public class CreateTableCommand {

    public CreateTableCommand(){
        create();
    }

    private void create(){
        if ( checkExists(System.getProperty("user.dir"), "RobotWorlds") ){
            System.out.println("Your tables have already been set up.");

        }else {
            System.out.println(
                    "   Creating Tables...\n" +
                    "<=====================>");
            WorldData.createWorldObjectsTable();
            WorldData.createServerWorldsTable();
        }

    }

    public static boolean checkExists(String directory, String file) {
        File dir = new File(directory);
        File[] dir_contents = dir.listFiles();
        String temp = file + ".db";
        boolean check = new File(directory, temp).exists();

        System.out.println(" > Checking if \"RobotWorlds.db\" file exists...");
        System.out.println(" > Found Files: "+Arrays.toString(dir_contents));
        System.out.println(" > Check boolean: "+check);  // -->always says false

        for(int i = 0; i < dir_contents.length; i++) {
            if(dir_contents[i].getName().equals(file + ".db")){
                return true;
            }
        }
        return false;

    }

}
