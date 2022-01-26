package za.co.wethinkcode.Domain.Robot;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduleTimer {
    private final String command;
    private final Timer timer;
    private final Robot robot;
    private final int seconds;

    public ScheduleTimer(Robot robot, String command, int seconds) {
        this.timer = new Timer();
        this.command = command;
        this.seconds = seconds;
        this.robot = robot;
        startTask();
    }

    public void startTask() {
        timer.schedule(new Task(), seconds * 1000l);
    }

    public void doRepair() {
        robot.setStatus("NORMAL");
        robot.setMessage("Done");

    }
    class Task extends TimerTask {
        public void run() {
            if(command.equals("repair")) {
                doRepair();
            }
            timer.cancel();
        }
    }
}
