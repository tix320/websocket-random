package org.example;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class ChatServer extends WebSocketServer {

    private final Set<String> connectedUsers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public ChatServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String ip = extractIp(conn);
        if (!connectedUsers.add(ip)) {
            conn.close(409, "Only one connection from same ip is allowed");
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String ip = extractIp(conn);
        connectedUsers.remove(ip);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = Math.abs(uuid.getMostSignificantBits());
        long leastSignificantBits = Math.abs(uuid.getLeastSignificantBits());
        String randomNumber = "" + mostSignificantBits + leastSignificantBits;
        conn.send(randomNumber);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    private String extractIp(WebSocket webSocket) {
        return webSocket.getRemoteSocketAddress().getAddress().toString();
    }

    public static void main(String[] args) {
        int port = 8887;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ignored) {
        }

        ChatServer s = new ChatServer(port);
        s.start();
        System.out.println("ChatServer started on port: " + s.getPort());
    }
}
