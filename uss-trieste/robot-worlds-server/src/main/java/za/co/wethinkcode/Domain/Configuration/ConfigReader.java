package za.co.wethinkcode.Domain.Configuration;

import za.co.wethinkcode.Socket.SocketServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private int worldWidth;
    private int worldHeight;
    private int visibility;
    private int reloadTime;
    private int repairTime;
    private int setMineTime;
    private int worldSizes;
    private int maxShield;
    private int PORT;
    public static boolean worldSize = true;
    public static boolean portNumber = true;

    /**
     * This config file stores all the data that is specific to the world.
     */
    public ConfigReader() {
        InputStream inputStream;

        try {
            Properties properties = new Properties();
            String configurationFile = "configuration.properties";

            inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(configurationFile);

            if (inputStream == null) {
                throw new FileNotFoundException("configuration file '" + configurationFile + "' not found.");
            } else {
                properties.load(inputStream);
            }

            this.PORT = Integer.parseInt(properties.getProperty("PORT"));
            this.visibility = Integer.parseInt(properties.getProperty("visibility"));
            this.reloadTime = Integer.parseInt(properties.getProperty("reloadTime"));
            this.repairTime = Integer.parseInt(properties.getProperty("repairTime"));
            this.setMineTime = Integer.parseInt(properties.getProperty("setMineTime"));
            this.maxShield = Integer.parseInt(properties.getProperty("maxShield"));
            this.worldWidth = Integer.parseInt(properties.getProperty("worldWidth"));
            this.worldHeight = Integer.parseInt(properties.getProperty("worldHeight"));
            this.worldSizes = Integer.parseInt(properties.getProperty("worldSize"));


        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public int getWorldWidth() {
        if (worldSize){ return worldWidth; }
            return SocketServer.customWorldSize;
        }

    public int getWorldHeight() {
        if (worldSize){ return worldHeight; }
        return SocketServer.customWorldSize;
    }

    public int getPORT() {
        if (portNumber){ return PORT; }
        return SocketServer.customPort;
    }

    public int getVisibility() {
        return visibility;
    }

    public int getReloadTime(){ return reloadTime; }

    public int getRepairTime() { return repairTime; }

    public int getSetMineTime() { return setMineTime; }

    public int getMaxShield(){ return maxShield; }

    public int getWorldSizes() {
        if (worldSize){ return worldSizes; }
        return SocketServer.customWorldSize;
    }

    public void setWorldSizes(int worldSizes) {
        this.worldSizes = worldSizes;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }
}
