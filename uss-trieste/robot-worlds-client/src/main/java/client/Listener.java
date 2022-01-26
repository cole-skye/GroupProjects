package client;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Is a Thread-based class that is used to listen to any input the user receives from the socket server.
 */
public class Listener implements Runnable{
    private BufferedReader input;
    String msgFromServer = "";

    public Listener(BufferedReader in) {this.input = in;}

    public void run(){
        while (msgFromServer != null) {
            try {
                msgFromServer = input.readLine();
                System.out.println(msgFromServer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
