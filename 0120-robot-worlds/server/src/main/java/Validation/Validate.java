package Validation;

public class Validate {

    public static boolean isPositiveInteger(String strNumber) {
        int number;
        try {
            number = Integer.parseInt(strNumber);
        } catch (Exception e) {
            return false;
        }
        return (number > 0);
    }

    public static boolean isRobotType(String type) {
        type = type.trim().toLowerCase();
        if (type.equals("normal")) return true;
        return false;
    }

}
