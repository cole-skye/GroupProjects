package za.co.wethinkcode.Socket;

import org.apache.commons.cli.*;

import za.co.wethinkcode.Communication.Communication;
import za.co.wethinkcode.Domain.Configuration.ConfigReader;
import za.co.wethinkcode.Domain.Map.EmptyMap;
import za.co.wethinkcode.Domain.ServerCommands.CommandListen;
import za.co.wethinkcode.Domain.world.World.AbstractWorld;
import za.co.wethinkcode.Domain.world.WorldObjects.Obstacle.SquareObstacle;
import za.co.wethinkcode.Domain.world.World.World;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

// SocketServer class
public class SocketServer
{

    public static ConfigReader configReader = new ConfigReader();

    // Vector to store active clients
    public static Vector<Communication> clientList = new Vector<>();

    private static final Scanner scn = new Scanner( System.in );

    private static final List<WorldObject> customObstacleList = new ArrayList<>();

    //World
    private static AbstractWorld world =  new World(new EmptyMap());

    private static int PORT;

    public static int customWorldSize;

    public static int customPort;

    public static String[] obstacle;

    public static int customObstacleX;

    public static int customObstacleY;


    public static void setWorld(AbstractWorld selectedMaze){
        world = selectedMaze;
    }

    // Allows Robot to get the world from the SocketServer.
    public static AbstractWorld getWorld(){
        return world;
    }

    public static Vector<Communication> getClientList(){
        return clientList;
    }

    /**
     * Shuts down the SocketServer.
     */
    public static void Quit(){
        for ( Communication client : getClientList() ) {
            client.dos.println(" >> You have been disconnected from the SocketServer");
        }
        getWorld().reset();

        System.out.println(Arrays.toString(getWorld().getObstacles().toArray()));
        System.out.println(Arrays.toString(getWorld().getRobotList().toArray()));

        System.exit(1);
    }

    // private static FileWriter file;
    public static void main(String[] args) throws IOException
    {
        // Gets the port number
        Options options = new Options();
        Option port = new Option("p", "port", true, "Port Number");
        port.setRequired(false);
        options.addOption(port);

        // gets the world size
        Option worldSize = new Option("s", "worldSize", true, "World Size");
        worldSize.setRequired(false);
        options.addOption(worldSize);

        // gets the obstacles
        Option obstacles = new Option("o", "Obstacles", true, "Obstacles");
        obstacles.setRequired(false);
        options.addOption(obstacles);

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Command specs", options);
            System.exit(1);
            return;
        }

        if (cmd.hasOption("p")) {
            // assigns port ID provided
            ConfigReader.portNumber = false;
            customPort = Integer.parseInt(cmd.getOptionValue("port"));
            configReader.setPORT(Integer.parseInt((cmd.getOptionValue("port"))));

        } else {
            //assigns default port ID
            ConfigReader.portNumber = true;
        }

        if (cmd.hasOption("s")) {
            // assigns custom world size
            ConfigReader.worldSize = false;
            customWorldSize = Integer.parseInt(cmd.getOptionValue("worldSize"));

            getWorld().setWorldSize(Integer.parseInt((cmd.getOptionValue("worldSize"))));

        } else {
            // assigns default world size
            ConfigReader.worldSize = true;
        }

        if (cmd.hasOption("o")) {
            // assign obstacles
            obstacle = cmd.getOptionValue("Obstacles").split(",");

            customObstacleX = Integer.parseInt(obstacle[0]);
            customObstacleY = Integer.parseInt(obstacle[1]);

            WorldObject newObstacle = new SquareObstacle(customObstacleX, customObstacleY);
            customObstacleList.add(newObstacle);

            getWorld().setObstacles(customObstacleList);

            System.out.println(" > Obstacles: " + cmd.getOptionValue("Obstacles"));
        }

        // print world
        System.out.println(" > Generating World... "+"[ currentWorld: "+world.getWorldName()+", custom_size: "+cmd.getOptionValue("worldSize")+"x"+cmd.getOptionValue("worldSize")+", custom_obstacles: "+cmd.getOptionValue("Obstacles")+", visibility: "+configReader.getVisibility()+" ]");

        // assigns port
        PORT = configReader.getPORT();
        System.out.println(" > Robot World server running and waiting for client connections on port "+PORT);

        // server is listening on port
        ServerSocket ss = new ServerSocket(PORT);

        Socket socket;

        CommandListen commandListen = new CommandListen();
        // get input for server commands
        Thread commandlistener = new Thread(commandListen);
        commandlistener.start();

        // running infinite loop for getting
        // client request
        while (true)
        {
            // Accept the incoming request
            socket = ss.accept();

            System.out.println(" > New client request received : " + socket);

            // obtain input and output streams
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream dataOut = new PrintStream(socket.getOutputStream());

            System.out.println(" > Creating a new handler for this client...");

            // Create a new handler object for handling this request.
            Communication clientN = new Communication(dataOut, dataIn, world);

            // Create a new Thread with this object.
            Thread thread = new Thread(clientN);

            // add this client to active clients list
            clientList.add(clientN);

            // start the thread.
            thread.start();

        }
    }
}
