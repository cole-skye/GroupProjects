package Robot.Types;

import Robot.*;

public class NormalRobot extends AbstractRobot {

    private int shieldStrength;
    private int gunDistance;
    private int maxShots;

    public NormalRobot(String name) {
        super(name, "normal");
        shieldStrength = 5;
        gunDistance = 5;
        maxShots = 5;
    }

}
