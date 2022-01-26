package Config;

import Robot.Position;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class fileUtils {

    private static JSONObject createBoundaryJsonObject(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY) {
        JSONObject boundary;
        JSONArray topLeft;
        JSONArray bottomRight;

        boundary = new JSONObject();
        topLeft = new JSONArray();
        bottomRight = new JSONArray();
        topLeft.add(topLeftX);
        topLeft.add(topLeftY);
        boundary.put("topLeftPosition", topLeft);
        bottomRight.add(bottomRightX);
        bottomRight.add(bottomRightY);
        boundary.put("bottomRightPosition", bottomRight);
        return boundary;
    }

    private static JSONObject createWorldJson() {
        JSONObject obj;

        obj = new JSONObject();
        //boundary coordinates (bottomLeft/topRight)
        obj.put("boundary", createBoundaryJsonObject(-200,200,200,-200));
        //boundary width in steps
        obj.put("boundaryWidth", 400);
        //boundary height in steps
        obj.put("boundaryHeight", 400);
        //amount of steps the robot can see forward/right/back/left
        obj.put("visibility", 10);
        //shield repair time in seconds
        obj.put("shieldRepairTime", 10);
        //weapon reload time in seconds
        obj.put("weaponReloadTime", 10);
        //mine set time in seconds
        obj.put("mineSetTime", 10);
        //shield strength max in hits
        obj.put("maxShieldStrength", 10);
        return obj;
    }

    private static String getFilePath(String filename) throws IOException {
        File f;

        f = new File(filename);
        if (!f.exists()) {
            f.createNewFile();
        }
        return f.getAbsolutePath();
    }

    public static boolean addJsonToFile() {
        FileWriter writer;
        String filename;

        filename = "src/main/java/Config/.config.txt";
        try {
            writer = new FileWriter(getFilePath(filename));
            writer.write(createWorldJson().toString());
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static JSONObject getJsonFileContent(String filepath) {
        JSONParser parser;

        parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(new FileReader(filepath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getValueFromJson(JSONObject obj, String key) {
        return obj.get(key);
    }

    public static Position getBoundaryTopLeft() {
        JSONObject obj;
        JSONArray topLeft;
        int x;
        int y;

        try {
            obj = getJsonFileContent(getFilePath("src/main/java/Config/.config.txt"));
            topLeft = (JSONArray) getValueFromJson((JSONObject) obj.get("boundary"), "topLeftPosition");
            x = ((Number)topLeft.get(0)).intValue();
            y = ((Number)topLeft.get(1)).intValue();
            return new Position(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Position getBoundaryBottomRight() {
        JSONObject obj;
        JSONArray bottomRight;
        int x;
        int y;

        try {
            obj = getJsonFileContent(getFilePath("src/main/java/Config/.config.txt"));
            bottomRight = (JSONArray) getValueFromJson((JSONObject) obj.get("boundary"), "bottomRightPosition");
            x = ((Number)bottomRight.get(0)).intValue();
            y = ((Number)bottomRight.get(1)).intValue();
            return new Position(x, y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getBoundaryWidth() {
        JSONObject obj;

        try {
            obj = getJsonFileContent(getFilePath("src/main/java/Config/.config.txt"));
            return ((Number)getValueFromJson(obj, "boundaryWidth")).intValue();
        } catch (Exception e) {
            return -1;
        }
    }

    public static int getBoundaryHeight() {
        JSONObject obj;

        try {
            obj = getJsonFileContent(getFilePath("src/main/java/Config/.config.txt"));
            return ((Number)getValueFromJson(obj, "boundaryHeight")).intValue();
        } catch (Exception e) {
            return -1;
        }
    }

    public static int getVisibility() {
        JSONObject obj;

        try {
            obj = getJsonFileContent(getFilePath("src/main/java/Config/.config.txt"));
            return ((Number)getValueFromJson(obj, "visibility")).intValue();
        } catch (Exception e) {
            return -1;
        }
    }

}
