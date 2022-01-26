package Robot.Types;

import Robot.AbstractRobot;

public class BomberRobot extends AbstractRobot {

    private int shieldStrength;

    public BomberRobot(String name){
        super(name, "bomber");
        shieldStrength = 5;
    }
}
