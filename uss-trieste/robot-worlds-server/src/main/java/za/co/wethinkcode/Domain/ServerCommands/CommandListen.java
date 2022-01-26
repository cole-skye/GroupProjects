package za.co.wethinkcode.Domain.ServerCommands;

import java.io.IOException;
import java.util.Scanner;

/**
 * Checks for valid commands otherwise gives an error message
 * stating it is an unsupported command.
 */
public class CommandListen implements Runnable {
    // scanner
    static Scanner scanner = new Scanner(System.in);

    String[] command;
    String name;
    String argument;


    public void executeCommand(String instruction){
        this.command = instruction.split(" ");
        this.name = command[0];
        if (command.length > 1) {
            this.argument = command[1];
        }

        String[] args = instruction.toLowerCase().trim().split(" ");
        switch (args[0]){
            case "save":
                new SaveWorldCommand();
                break;
            case "restore":
                new RestoreWorldCommand();
                break;
            case "dump":
                new DumpCommand();
                break;
            case "purge":
                try{
                    new PurgeCommand(args[1]);
                } catch (IndexOutOfBoundsException | IOException e){
                    System.err.println(" > Error: " + e.getMessage());
                }
                break;
            case "quit":
                new QuitCommand();
                break;
            case "create":
                new CreateTableCommand();
                break;
            default:
                System.out.println(" > Unsupported command: " + args[0]);
        }
    }

    /**
     * Displays valid commands while server is running.
     * Prompts user for input.
     */
    @Override
    public void run() {

        // get input for server commands
        while (true) {

            System.out.println(
                    "                << ================================== >>      \n" +
                            "       *----------------------------------------------------* \n" +
                            "       | Valid Command: [ save, restore, create, dump, purge ]| \n" +
                            "       *----------------------------------------------------* \n" +
                            "                << ================================== >>      \n" +
                            " > Enter Command: "
            );
            String instruction = scanner.nextLine();
            executeCommand(instruction);
        }
    }
}
