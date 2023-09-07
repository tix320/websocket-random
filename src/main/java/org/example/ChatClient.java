package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ChatClient {

   static WebSocketClient cc;

    public static void main(String[] args) throws URISyntaxException, IOException {
        String location;
        if (args.length != 0) {
            location = args[0];
            System.out.println("Default server url specified: \'" + location + "\'");
        } else {
            location = "ws://localhost:8887";
            System.out.println("Default server url not specified: defaulting to \'" + location + "\'");
        }

         cc = new WebSocketClient(new URI(location)) {

            @Override
            public void onMessage(String message) {
                System.out.println("Number recieved: " + message);
            }

            @Override
            public void onOpen(ServerHandshake handshake) {
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };

        cc.connect();

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            if (in.equals("exit")) {
                cc.close();
                break;
            }else{
                cc.send("");
            }
        }
    }

}
