package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entityOther.WebsocketPackage;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketIOService {

    @Autowired
    private SocketIOServer server;

    private final Map<String, UUID> sessions = new ConcurrentHashMap<>();

    @PostConstruct
    public void start() {
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("message", String.class, onMessageReceived());
        server.start();
    }

    @PreDestroy
    public void stop() {
        server.stop();
    }

    private ConnectListener onConnected() {
        return socketIOClient -> {
            String token = socketIOClient.getHandshakeData().getSingleUrlParam("token");
            System.out.println("Connection established with: " + token);
            sessions.put(token, socketIOClient.getSessionId());
        };
    }

    private DisconnectListener onDisconnected() {
        return socketIOClient -> {
            String token = socketIOClient.getHandshakeData().getSingleUrlParam("token");
            System.out.println("Connection closed with: " + token);
            sessions.remove(token);
        };
    }

    private DataListener<String> onMessageReceived() {
        return (socketIOClient, message, ackRequest) -> {
            // Handle incoming messages
        };
    }

    public void sendWebsocketPackage(String token, WebsocketPackage websocketPackage) {
        UUID sessionId = sessions.get(token);

        if (sessionId != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(websocketPackage);
                System.out.println("Sending message to " + token + " : " + jsonMessage);
                server.getClient(sessionId).sendEvent("message", jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Session for: " + token + " is null or closed");
        }
    }
}