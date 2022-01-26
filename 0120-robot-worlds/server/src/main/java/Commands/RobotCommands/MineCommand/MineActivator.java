package Commands.RobotCommands.MineCommand;

import Robot.Robot;
import World.Mine;

public class MineActivator {
    Mine mine;
    Robot robot;

    public MineActivator(Robot robot, Mine mine) {
        this.mine = mine;
        this.robot = robot;
    }

    public void start() {
        boolean isReady;
        int shieldStrength;

        shieldStrength = robot.getShieldStrength();
        robot.setShieldStrength(0);
        isReady = getMineReady();
        robot.setShieldStrength(shieldStrength);
        if (isReady) {
            mine.Activate();
            System.out.println("Mine has been activated.");
        }
    }

    private boolean getMineReady() {
        try {
            Thread.sleep(10000);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void activateMine() {
        (this).start();
    }

}