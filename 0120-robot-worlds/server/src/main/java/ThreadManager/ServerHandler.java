package ThreadManager;

import Commands.WorldCommand;
import World.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ServerHandler extends Thread {
    private World world;
    private ArrayList<ClientHandler> clients;

    public ServerHandler(World world, ArrayList<ClientHandler> clients) {
        this.world = world;
        this.clients = clients;
    }

    private void printHeading() {
        System.out.println("ROBOT WORLDS");
    }

    private String getCommand(String instruction) {
        String[] args;

        args = instruction.split(" ");
        return args[0].toLowerCase();
    }

    private String getArgs(String instruction) {
        int i;
        String strArgs;
        String[] args;

        i = 0;
        strArgs = "";
        args = instruction.split(" ");
        for (String arg: args) {
            if (i != 0) {
                strArgs += arg;
            }
            i++;
        }
        return strArgs;
    }

    /**
     * Retrieves input from the user.
     *
     * @param prompt the message to display before accepting input.
     * @return the input received from the user.
     */
    private static String getInput(String prompt) {
        String input;
        Scanner scan;

        scan = new Scanner(System.in);
        System.out.print(prompt);
        input = (scan.hasNextLine()) ? scan.nextLine() : "";
        while (input.isBlank()) {
            System.out.print(prompt);
            input = (scan.hasNextLine()) ? scan.nextLine() : input;
        }
        return input;
    }

    /**
     * Retrieves the command and command arguments from the user.
     *
     * @return a String consisting out of the chosen command and command arguments.
     */
    private static String getInstruction() {
        String[] valid;
        List<String> validCommands;
        String instruction;

        valid = new String[]{"quit","robots", "purge", "dump", "robot"};
        validCommands = new ArrayList<>(Arrays.asList(valid));
        instruction = getInput("<WORLD> : What should I do next? ");
        while (!(validCommands.contains(instruction.split(" ")[0]))) {
            System.out.print("INVALID: Please choose a valid command:\n\n"+
                            "Valid commands:\n"+
                            "- quit               : terminates program.\n"+
                            "- robots             : lists all robots, and their states, in the world.\n"+
                            "- purge <robot name> : removes the robot, with specified name, from the world.\n"+
                            "- dump               : removes all robots from the world.\n" +
                    "- robot              : shows the number of robots in the world.\n");
            instruction = getInput("<WORLD> : What should I do next? ");
        }
        return instruction;
    }

    @Override
    public void run() {
        String instruction;
        WorldCommand worldCommand;
        boolean shouldContinue;

        printHeading();
        while (true) {
            instruction = getInstruction();
            worldCommand = WorldCommand.create(getCommand(instruction), getArgs(instruction), clients);
            shouldContinue = worldCommand.execute(world);
            System.out.println(world.getStatus());
            if (!shouldContinue) break;
        }
    }

}
