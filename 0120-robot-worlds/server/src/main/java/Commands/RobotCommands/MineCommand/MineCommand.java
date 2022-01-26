package Commands.RobotCommands.MineCommand;

import Commands.RobotCommand;
import JsonUtils.JsonEncode;
import Robot.Robot;
import World.Mine;
import World.World.UpdateResponse;
import Robot.Position;
import World.World;
import org.json.simple.JSONObject;

public class MineCommand extends RobotCommand {

    public MineCommand(){super("mine");}

    /**
     * Takes into consideration the robots current Direction,
     * then places the mine at the appropriate position.
     * @param target Robots position.
     * @param world used for the setMine param to access the mines list.
     * @return the status and message that will be sent to the client.
     */
    @Override
    public JSONObject execute(Robot target, World world) {
        int mineBottomLeftX;
        int mineBottomLeftY;
        Mine mine;
        MineActivator activator;

        mineBottomLeftX = target.getPosition().getX();
        mineBottomLeftY = target.getPosition().getY();
        if (target.getDirection() == Robot.Direction.NORTH){
            mineBottomLeftY = mineBottomLeftY - 4;
        } else if (target.getDirection() == Robot.Direction.SOUTH){
            mineBottomLeftY = mineBottomLeftY + 4;
        } else if (target.getDirection() == Robot.Direction.EAST){
            mineBottomLeftX = mineBottomLeftX - 4;
            mineBottomLeftY = mineBottomLeftY + 4;
        } else {
            mineBottomLeftX = mineBottomLeftX + 4;
        }
        mine = createMine(new Position(mineBottomLeftX , mineBottomLeftY), world);
        activator = new MineActivator(target, mine);
        moveRobotForwardOneStep(target, world);
        activator.activateMine();
        return createMineResponse(target);
    }

    /**
     * Creates Mine object behind the Robot object's position.
     * @param position the Mine object's bottom left Position object.
     * @param world the World object the Robot object exists in.
     * @return the Mine object created in the world.
     */
    private Mine createMine(Position position, World world){
        int x;
        int y;
        Mine mine;

        x = position.getX();
        y = position.getY();
        mine = new Mine(x, y);
        world.addMine(mine);
        return mine;
    }

    private void moveRobotForwardOneStep(Robot target, World world) {
        UpdateResponse response;

        response = world.updatePosition(target, 1);
    }

    /**
     * Status and message that will be sent to the client server through JSON files.
     * @param target setStatus and setMessage.
     * @return the response that will be sent to the encoder.
     */
    private JSONObject createMineResponse(Robot target){
        JsonEncode encoder;
        String result;

        result = "OK";
        encoder = new JsonEncode(target, result, "mine");
        encoder.setStatus("SETMINE");
        return encoder.response;
    }

}