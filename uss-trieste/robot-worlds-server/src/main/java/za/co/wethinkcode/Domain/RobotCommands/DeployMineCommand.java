package za.co.wethinkcode.Domain.RobotCommands;

import za.co.wethinkcode.Domain.Robot.Position;
import za.co.wethinkcode.Domain.Robot.Robot;
import za.co.wethinkcode.Domain.world.WorldObjects.Mine.LandMine;
import za.co.wethinkcode.Domain.world.WorldObjects.WorldObject;

import java.io.IOException;


/**
 * Deploys a mine on the position then moves the robot forward a step.
 */
public class DeployMineCommand extends Command{

    public DeployMineCommand(){
        super("mine");
    }

    @Override
    public boolean execute(Robot target) throws IOException {

        if (target.getNumberOfMines() > 0){
            target.setStatus("SETMINE");
            target.setStatus("NORMAL");
            WorldObject mine = new LandMine(target.getPosition().getX(), target.getPosition().getY());

            Position newPosition = new Position(target.getPosition().getX(), target.getPosition().getY() + 1);
            target.setPosition( newPosition );

            target.addMine(mine);
            target.setMessage("Done");

            target.usedAMine();
            target.setMessage("Deploying Mine, mines left: " + target.getNumberOfMines());
        }
        else{
            target.setMessage("Out of mines!");
        }
        return true;
    }
}
