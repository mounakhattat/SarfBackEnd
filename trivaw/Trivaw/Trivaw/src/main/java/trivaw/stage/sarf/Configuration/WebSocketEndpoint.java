package trivaw.stage.sarf.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import trivaw.stage.sarf.Entities.Reservation;
import trivaw.stage.sarf.services.BureauDeChangeServices;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketEndpoint implements WebSocketHandler {
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();


    @Autowired
    BureauDeChangeServices bureauDeChangeServices;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session); // Ajouter la session à la liste
        System.out.println("WebSocket opened: " + session.getId());

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        System.out.println("Received from " + session.getId() + ": " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session); // Retirer la session de la liste lorsqu'elle est fermée
        System.out.println("WebSocket closed: " + session.getId() + ", status: " + status.toString());

    }

    public static CopyOnWriteArrayList<WebSocketSession> getSessions() {
        return sessions;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Gérer les erreurs de transport
        System.out.println("WebSocket transport error for session " + session.getId() + ": " + exception.getMessage());

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}