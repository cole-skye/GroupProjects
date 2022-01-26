package za.co.wethinkcode.Domain.ServerCommands;

import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Socket.SocketServer;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

/**
 * Displays the world's state showing robots, obstacles, pits, mines
 * and anything else in the world that you programmed.
 */
public class DumpCommand {

    public DumpCommand(){
        dump();
    }

    private void dump(){
        System.out.println(" > Robot World's current state:");
        System.out.println(outputWorldSize());
        System.out.print(outputRobots());
        System.out.print(outputObs());
        System.out.print(outputPits());
        System.out.print(outputMines());

    }

    private String outputObs() {
        String obsOutput = "Obstacles: \n";
        if (SocketServer.getWorld().getObstacles().isEmpty()){
            obsOutput = obsOutput + " > No obstacles in world.\n";
        }
        for (WorldObject obs : SocketServer.getWorld().getObstacles()){
            String obsPosition = " > "+obs.getAsString()+"\n";
            obsOutput += obsPosition;
        }
        return obsOutput;
    }

    private String outputRobots() {
        String robotsOutput = "Robots: \n";
        if (SocketServer.getWorld().getRobotList().isEmpty()){
            robotsOutput += " > No robots in world.\n";
        }
        for (Robot robot : SocketServer.getWorld().getRobotList()){
            String robotPos = " > "+robot.getName() + ": (" + robot.getPosition().getX() + ", " + robot.getPosition().getY() + ")\n";
            robotsOutput += robotPos;
        }

        return robotsOutput;
    }

        private String outputPits() {
        String pitOutput = "Pits:\n";
        if (SocketServer.getWorld().getPits().isEmpty()){
            pitOutput = pitOutput + " > No pits in world.\n";
        }
        for (WorldObject pit : SocketServer.getWorld().getPits()){
            String pitPosition = " > "+pit.getAsString()+"\n";
            pitOutput += pitPosition;
        }
        return pitOutput;
    }

    private String outputMines() {
        String minesOutput = "Mines:\n";
        if (SocketServer.getWorld().getMines().isEmpty()){
            minesOutput = minesOutput + " > No mines currently in world.\n";
        }
        for (WorldObject mine : SocketServer.getWorld().getMines()){
            String minePosition = " > (" + mine.getX() + ", " + mine.getY() +")\n";
            minesOutput += minePosition;
        }
        return minesOutput;
    }

    private String outputWorldSize(){
        return "World Size: \n > "+ SocketServer.getWorld().getWorldSize()+"x"+ SocketServer.getWorld().getWorldSize();
    }
}
