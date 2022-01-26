package ThreadManager;

public class ErrorMessages {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";

    private static void printError(String errorMessage) {
        System.out.println(ANSI_RED+errorMessage+ANSI_RESET);
    }

    public static void printErrorLaunch() {
        String error;

        error = "ERROR: Chosen WORLD already contains ROBOT type.\nChoose a different WORLD, or ROBOT type, to launch robot.";
        printError(error);
    }

    public static void printConnectionError() {
        String error;

        error = "ERROR: ROBOT could not connect to WORLD.";
        printError(error);
    }


}
