package Commands;

public class ShutdownCommand {

    public static void execute(String message) {
        System.out.println("\n"+message);
        System.exit(1);
    }

}
