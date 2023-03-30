package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entityOther.WebsocketPackage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Handle incoming messages
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        String token = session.getUri().getPath().substring("/topic/".length());
        System.out.println("Connection established with: " + token);
        sessions.put(token, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Remove the session from the sessions map
        //sessions.values().removeIf(s -> s.getId().equals(session.getId()));
    }



    public void sendWebsocketPackage(String token, WebsocketPackage websocketPackage) {
            WebSocketSession session = sessions.get(token);

            if (session != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonMessage = objectMapper.writeValueAsString(websocketPackage);
                    System.out.println("Sending message to " + token + " : " + jsonMessage);
                    session.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{

                System.out.println("Session for: " + token + " is null or closed");
                session.isOpen();
            }
        }
}
