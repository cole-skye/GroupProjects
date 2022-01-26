package Commands.RobotCommands;

import Commands.RobotCommand;
import JsonUtils.JsonEncode;
import Robot.*;
import ThreadManager.ClientHandler;
import World.*;
import org.json.simple.JSONObject;

import java.util.List;

public class FireCommand extends RobotCommand {
    private static final int maxDistance = 5;
    private List<ClientHandler> clients;

    public FireCommand(List<ClientHandler> clients) {
        super("fire");
        this.clients = clients;
    }

    @Override
    public JSONObject execute(Robot shooter, World world) {
        int shotsLeft;
        int currentDistance;
        boolean executed;
        Robot shotRobot;
        JSONObject shotRobotResponse;

        shotsLeft = shooter.getShotsLeft();
        currentDistance = shooter.getShotDistance();
        executed = false;
        shotRobot = null;
        shotRobotResponse = new JSONObject();
        if (shotsLeft < 1) {
            return createShooterRobotResponse(shooter, shotRobot, executed, shotRobotResponse);
        } else if (currentDistance > 0) {
            shotRobot = makeShot(shooter, world, currentDistance);
            shotRobot = updateShotRobot(shotRobot);
            shooter = updateShooterRobot(shooter);
        }
        if (shotRobot != null) {
            executed = true;
            shotRobotResponse = sendResponseToShotRobot(shotRobot);
            if ((shotRobot.getShieldStrength() < 0)) world.removeRobot(shotRobot);
        }
        return createShooterRobotResponse(shooter, shotRobot, executed, shotRobotResponse);
    }

    private JSONObject createShotRobotResponse(Robot shotRobot) {
        JsonEncode encoder;
        JSONObject state;

        encoder = new JsonEncode(shotRobot, "OK", "fire");
        if (shotRobot.getShieldStrength() < 0) {
            encoder.setStatus("DEAD");
        }
        state = encoder.getState();
        return state;
    }

    private void sendResponseToClient(Robot robot, JSONObject response) {
        for (ClientHandler client: clients) {
            if (client.getRobot().equals(robot)) {
                client.sendToClient(response);
                return;
            }
        }
    }

    private JSONObject sendResponseToShotRobot(Robot shotRobot) {
        JSONObject response;

        response = createShotRobotResponse(shotRobot);
        if (response != null) sendResponseToClient(shotRobot, response);
        return response;
    }

    private Position getObstructedBulletDestination(Obstacle obstacle, Robot shooter) {
        int bulletX;
        int bulletY;

        bulletX = shooter.getPosition().getX();
        bulletY = shooter.getPosition().getY();
        switch (shooter.getDirection()) {
            case NORTH:
                bulletY = obstacle.getBottomLeftY();
                break;
            case EAST:
                bulletX = obstacle.getBottomLeftX();
                break;
            case SOUTH:
                bulletY = obstacle.getTopRightY();
                break;
            case WEST:
                bulletX = obstacle.getTopRightX();
                break;
        }
        return new Position(bulletX, bulletY);
    }

    private Position getBulletFinalPosition(int currentShotDistance, List<Obstacle> obstacles, Robot shooter) {
        int newX;
        int newY;
        Position bulletPosition;

        newX = shooter.getPosition().getX();
        newY = shooter.getPosition().getY();
        switch (shooter.getDirection()) {
            case NORTH:
                newY = newY + currentShotDistance;
                break;
            case EAST:
                newX = newX + currentShotDistance;
                break;
            case SOUTH:
                newY = newY - currentShotDistance;
                break;
            case WEST:
                newX = newX - currentShotDistance;
                break;
        }
        bulletPosition = new Position(newX, newY);
        for (Obstacle obstacle: obstacles) {
            if (obstacle.blockPath(shooter.getPosition(), bulletPosition)) {
                return getObstructedBulletDestination(obstacle, shooter);
            }
        }
        return bulletPosition;
    }

    private Robot makeShot(Robot shooter, World world, int currentDistance) {
        List<Robot> robots;
        Position bulletPosition;

        robots = world.getAllRobots();
        bulletPosition = getBulletFinalPosition(currentDistance, world.getObstacles(), shooter);
        for (Robot robot : robots) {
            if (robot.equals(shooter)) continue;
            if (shooter.robotBlocksPath(bulletPosition, robot.getPosition())) {
                return robot;
            }
        }
        return null;
    }

    public Robot updateShotRobot(Robot shotRobot) {
        if (shotRobot != null) {
            shotRobot.setShieldStrength(shotRobot.getShieldStrength()-1);
        }
        return shotRobot;
    }

    public Robot updateShooterRobot(Robot shooter) {
        int shotsLeft;
        int shotDistance;

        shotsLeft = shooter.getShotsLeft();
        shotDistance = shooter.getShotDistance();
        if (shooter.getShotsLeft() > 0 && shotDistance > 0) {
            shooter.setShotsLeft(shotsLeft-1);
            shooter.setShotDistance(shotDistance-1);
        }
        return shooter;
    }

    private int getShotRobotDistance(Robot shooter, Robot shotRobot) {
        Position shooterPos;
        Position shotRobotPos;

        shooterPos = shooter.getPosition();
        shotRobotPos = shotRobot.getPosition();
        switch (shooter.getDirection()) {
            case NORTH:
                return (shotRobotPos.getY() - shooterPos.getY());
            case EAST:
                return (shotRobotPos.getX() - shooterPos.getX());
            case SOUTH:
                return (Math.abs(shotRobotPos.getY()) + shooterPos.getY());
        }
        //east
        return (Math.abs(shotRobotPos.getX()) + shooterPos.getX());
    }

    private JSONObject createShooterRobotResponse(Robot shooter, Robot shotRobot, boolean executed, JSONObject shotRobotResponse) {
        JsonEncode encoder;
        String shotRobotName;
        JSONObject shotRobotState;
        int shotRobotDistance;

        shotRobotName = "";
        shotRobotState = new JSONObject();
        shotRobotDistance = 0;
        if (shotRobot != null) {
            shotRobotName = shotRobot.getName();
            shotRobotState = (JSONObject) shotRobotResponse.get("state");
            shotRobotDistance = getShotRobotDistance(shooter, shotRobot);
        }
        encoder = new JsonEncode(shooter, "OK", "fire");
        encoder.createFireCommandResponse(executed, shotRobotDistance, shotRobotName, shotRobotState, shooter.getShotsLeft());
        return encoder.response;
    }

    public static int getMaxShotDistance() {
        return maxDistance;
    }
}
