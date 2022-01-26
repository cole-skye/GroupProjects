package za.co.wethinkcode.CurriculumClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class RobotWorldJsonClient implements RobotWorldClient {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Socket socket;
    private PrintStream out;
    private BufferedReader in;

    /**
     * Connect to a server with parameter IP and Port
     * @param ipAddress either `localhost` or actual IP address
     * @param port port that server is configured to receive connections on
     */
    @Override
    public void connect(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        } catch (IOException e) {
            //error connecting should just throw Runtime error and fail test
            throw new RuntimeException("Error connecting to Robot Worlds server.", e);
        }
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public void disconnect() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            //error connecting should just throw Runtime error and fail test
            throw new RuntimeException("Error disconnecting from Robot Worlds server.", e);
        }
    }

    /**
     * Send a request to the sever as a JSON node
     * @param requestJsonString a String representing the Json string to send to server
     * @return JsonNode
     */
    @Override
    public JsonNode sendRequest(String requestJsonString) {
        try {
            out.println(requestJsonString);
            out.flush();
            return OBJECT_MAPPER.readTree(in.readLine());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing server response as JSON.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading server response.", e);
        }
    }

    /**
     * Sends a request to the server as a JSON String
     * @param requestString
     * @return
     */
    @Override
    public String sendRequestAsString(String requestString) {
        try {
            out.println(requestString);
            out.flush();
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading server response.", e);
        }
    }
}
