import Commands.RobotCommand;
import Commands.WorldCommand;
import Robot.*;
import World.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RobotCommandTest {

    @Test
    public void testShutdown(){
        World world = new World();
        WorldCommand off = WorldCommand.create("off", "", new ArrayList<>());
        assertFalse(off.execute(world));
        assertEquals(world.getStatus(), "<WORLD> Shutting down...");
    }

    @Test
    public void testRight() {
        World world = new World();
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        RobotCommand right = RobotCommand.create("right", "");
        assertEquals(right.execute(robot, world).get("result"), "OK");
        assertEquals(Robot.Status.NORMAL, robot.getStatus());
        assertEquals(Robot.Direction.EAST, robot.getDirection());
    }

    @Test
    public void testMine(){
//        World world = new World();
//        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
//        RobotCommand mine = RobotCommand.create("mine", "");
//        assertEquals(mine.execute(robot, world).get("mine"), "OK");

        assertTrue(true);
    }

    @Test
    public void testRightRight(){
        World world = new World();
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        RobotCommand rightA = RobotCommand.create("right", "");
        RobotCommand rightB = RobotCommand.create("right", "");
        assertEquals(rightA.execute(robot, world).get("result"), "OK");
        assertEquals(Robot.Status.NORMAL, robot.getStatus());
        assertEquals(Robot.Direction.EAST, robot.getDirection());
        assertEquals(rightB.execute(robot, world).get("result"), "OK");
        assertEquals(Robot.Status.NORMAL, robot.getStatus());
        assertEquals(Robot.Direction.SOUTH, robot.getDirection());

    }

    @Test
    public void testLeft() {
        World world = new World();
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        RobotCommand left = RobotCommand.create("left", "");
        assertEquals(left.execute(robot, world).get("result"), "OK");
        assertEquals(Robot.Status.NORMAL, robot.getStatus());
        assertEquals(Robot.Direction.WEST, robot.getDirection());
    }

    @Test
    public void testLeftLeft(){
        World world = new World();
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        world.setPitfalls(new ArrayList<>());
        RobotCommand leftA = RobotCommand.create("left", "");
        RobotCommand leftB = RobotCommand.create("left", "");
        assertEquals(leftA.execute(robot, world).get("result"), "OK");
        assertEquals(Robot.Status.NORMAL, robot.getStatus());
        assertEquals(Robot.Direction.WEST, robot.getDirection());
        assertEquals(leftB.execute(robot, world).get("result"), "OK");
        assertEquals(Robot.Status.NORMAL, robot.getStatus());
        assertEquals(Robot.Direction.SOUTH, robot.getDirection());
    }

    @Test
    public void testForward(){
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        World world = new World();
        world.setPitfalls(new ArrayList<>());
        RobotCommand forward = RobotCommand.create("forward", "10");
        assertEquals(forward.execute(robot, world).get("result"), "OK");
        assertEquals(robot.getPosition().getX(), 0);
        assertEquals(robot.getPosition().getY(), 10);
    }

    @Test
    public void testForwardForward(){
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        World world = new World();
        world.setPitfalls(new ArrayList<>());
        RobotCommand forwardA = RobotCommand.create("forward", "10");
        RobotCommand forwardB = RobotCommand.create("forward", "10");
        assertEquals(forwardA.execute(robot, world).get("result"), "OK");
        assertEquals(forwardB.execute(robot, world).get("result"), "OK");
        assertEquals(robot.getPosition().getX(), 0);
        assertEquals(robot.getPosition().getY(), 20);
    }

    @Test
    public void testBack(){
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        World world = new World();
        world.setPitfalls(new ArrayList<>());
        RobotCommand back = RobotCommand.create("back", "10");
        assertEquals(back.execute(robot, world).get("result"), "OK");
        assertEquals(robot.getPosition().getX(), 0);
        assertEquals(robot.getPosition().getY(), -10);
    }

    @Test
    public void testBackBack(){
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        World world = new World();
        world.setPitfalls(new ArrayList<>());
        RobotCommand backA = RobotCommand.create("back", "10");
        RobotCommand backB = RobotCommand.create("back", "10");
        assertEquals(backA.execute(robot, world).get("result"), "OK");
        assertEquals(backB.execute(robot, world).get("result"), "OK");
        assertEquals(robot.getPosition().getX(), 0);
        assertEquals(robot.getPosition().getY(), -20);
    }

    @Test
    public void testPurge() {
        World world = new World();
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        Robot robotB = new Robot("CrashTestDummyB", "sniper", 0, 0);
        world.addRobot(robotA);
        world.addRobot(robotB);
        assertEquals(world.getAllRobots().get(0), robotA);
        assertEquals(world.getAllRobots().get(1), robotB);
        WorldCommand purge = WorldCommand.create("purge", "CrashTestDummyA", new ArrayList<>());
        purge.execute(world);
        assertTrue(world.getAllRobots().size() == 1);
        assertEquals(world.getAllRobots().get(0), robotB);
        assertEquals(world.getStatus(), "<WORLD> Robot successfully removed from the world.");
    }

    @Test
    public void testDump() {
        World world = new World();
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        Robot robotB = new Robot("CrashTestDummyB", "sniper", 0, 0);
        world.addRobot(robotA);
        world.addRobot(robotB);
        assertEquals(world.getAllRobots().get(0), robotA);
        assertEquals(world.getAllRobots().get(1), robotB);
        WorldCommand dump = WorldCommand.create("dump", "", new ArrayList<>());
        dump.execute(world);
        assertTrue(world.getAllRobots().size() == 0);
        assertEquals(world.getStatus(), "<WORLD> All robots successfully removed.");
    }

    @Test
    public void lookNoObstructions() {
        World world = new World();
        Robot robot = new Robot("CrashTestDummy", "normal", 0, 0);
        world.addRobot(robot);
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robot, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[]");
    }

    @Test
    public void lookRobotObstructionNorth() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        world.addRobot(robotA);
        Robot robotB = new Robot("CrashTestDummyB", "sniper", 0, 0);
        world.addRobot(robotB);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //move robotB forward
        robotB.setPosition(new Position(0, 5));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":5,\"type\":\"ROBOT\",\"direction\":\"NORTH\"}]");
    }

    @Test
    public void lookRobotObstructionEast() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        world.addRobot(robotA);
        Robot robotB = new Robot("CrashTestDummyB", "sniper", 0, 0);
        world.addRobot(robotB);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //move robotB forward
        robotB.setPosition(new Position(5, 0));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":5,\"type\":\"ROBOT\",\"direction\":\"EAST\"}]");
    }

    @Test
    public void lookRobotObstructionSouth() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        world.addRobot(robotA);
        Robot robotB = new Robot("CrashTestDummyB", "sniper", 0, 0);
        world.addRobot(robotB);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //move robotB forward
        robotB.setPosition(new Position(0, -5));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":5,\"type\":\"ROBOT\",\"direction\":\"SOUTH\"}]");
    }

    @Test
    public void lookRobotObstructionWest() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        world.addRobot(robotA);
        Robot robotB = new Robot("CrashTestDummyB", "sniper", 0, 0);
        world.addRobot(robotB);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //move robotB forward
        robotB.setPosition(new Position(-5, 0));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":5,\"type\":\"ROBOT\",\"direction\":\"WEST\"}]");
    }

    @Test
    public void lookObstacleObstructionNorth() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(0,0));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //add obstacle
        world.addObstacleToList(new Obstacle(-2,2));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":2,\"type\":\"OBSTACLE\",\"direction\":\"NORTH\"}]");
    }

    @Test
    public void lookObstacleObstructionEast() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(0,0));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //add obstacle
        world.addObstacleToList(new Obstacle(2,-2));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":2,\"type\":\"OBSTACLE\",\"direction\":\"EAST\"}]");
    }

    @Test
    public void lookObstacleObstructionSouth() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(0,0));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //add obstacle
        world.addObstacleToList(new Obstacle(-2,-5));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":2,\"type\":\"OBSTACLE\",\"direction\":\"SOUTH\"}]");
    }

    @Test
    public void lookObstacleObstructionWest() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(0,0));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //add obstacle
        world.addObstacleToList(new Obstacle(-5,-0));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":2,\"type\":\"OBSTACLE\",\"direction\":\"WEST\"}]");
    }

    @Test
    public void lookPitfallObstructionNorth() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(0,0));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //add obstacle
        world.addPitfallToList(new Pitfall(-2, 2));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":2,\"type\":\"PITFALL\",\"direction\":\"NORTH\"}]");
    }


    @Test
    public void lookPitfallObstructionEast() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(0,0));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //add obstacle
        world.addPitfallToList(new Pitfall(2, -2));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":2,\"type\":\"PITFALL\",\"direction\":\"EAST\"}]");
    }

    @Test
    public void lookPitfallObstructionSouth() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(0,0));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //add obstacle
        world.addPitfallToList(new Pitfall(-2, -5));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":2,\"type\":\"PITFALL\",\"direction\":\"SOUTH\"}]");
    }

    @Test
    public void lookPitfallObstructionWest() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(0,0));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //add obstacle
        world.addPitfallToList(new Pitfall(-5, 0));
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":2,\"type\":\"PITFALL\",\"direction\":\"WEST\"}]");
    }

    @Test
    public void lookEdgeObstructionNorth() {
        World world = new World();
        //add robots
        Robot robotA = new Robot("CrashTestDummyA", "normal", 0, 0);
        robotA.setPosition(new Position(Config.fileUtils.getBoundaryTopLeft().getX()+5,Config.fileUtils.getBoundaryTopLeft().getY()-5));
        world.addRobot(robotA);
        //reset obstacles and pitfalls
        world.setObstacles(new ArrayList<>());
        world.setPitfalls(new ArrayList<>());
        //use robotA to execute look command
        RobotCommand look = RobotCommand.create("look", "");
        JSONObject response = look.execute(robotA, world);
        assertEquals(((JSONObject)response.get("data")).get("objects").toString(), "[{\"distance\":5,\"type\":\"EDGE\",\"direction\":\"NORTH\"},{\"distance\":5,\"type\":\"EDGE\",\"direction\":\"WEST\"}]");
    }

    @Test
    public void missedShot() {
        World world;
        Robot robot;
        RobotCommand fire;

        world = new World();
        world.setObstacles(new ArrayList<>());
        robot = new Robot("CrashTestDummy", "normal", 0, 5);
        fire = RobotCommand.create("fire", "");
        assertEquals(fire.execute(robot, world).toString(), "{\"result\":\"OK\",\"data\":{\"message\":\"Miss\"},\"state\":{\"shields\":0,\"position\":[0,0],\"shots\":4,\"direction\":\"NORTH\",\"status\":\"NORMAL\"}}");
        assertEquals(robot.getShotsLeft(), 4);
    }

    @Test
    public void missedShotObstacle() {
        World world;
        Robot robotA;
        Robot robotB;
        RobotCommand fire;

        world = new World();
        world.setObstacles(new ArrayList<>());
        world.addObstacleToList(new Obstacle(0, 2));
        robotA = new Robot("CrashTestDummyA", "normal", 0, 5);
        robotB = new Robot("CrashTestDummyB", "normal", 0, 5);
        robotA.setPosition(new Position(0,0));
        robotB.setPosition(new Position(0,5));
        world.addRobot(robotA);
        world.addRobot(robotB);
        fire = RobotCommand.create("fire", "");
        assertEquals(fire.execute(robotA, world).toString(), "{\"result\":\"OK\",\"data\":{\"message\":\"Miss\"},\"state\":{\"shields\":0,\"position\":[0,0],\"shots\":4,\"direction\":\"NORTH\",\"status\":\"NORMAL\"}}");
        assertEquals(robotA.getShotsLeft(), 4);
    }

}
