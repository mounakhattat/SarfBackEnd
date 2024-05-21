package trivaw.stage.sarf.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.services.BureauDeChangeServices;
import trivaw.stage.sarf.services.UserDetailsImpl;
import trivaw.stage.sarf.services.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketEndpoint implements WebSocketHandler {
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Map<String, WebSocketSession> userSessionMap = new HashMap<>();

    private static final Map<String, WebSocketSession> userSessions = new HashMap<>();

    @Autowired
    BureauDeChangeServices bureauDeChangeServices;



    @Autowired
    UserService userService; // Assurez-vous d'injecter votre service utilisateur

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket opened: " + session.getId());
        System.out.println(session+"eedededededede");

        // Récupérer l'ID de l'utilisateur à partir de l'URL WebSocket
        String userIdString = session.getUri().getQuery().split("=")[1];
        System.out.println(userIdString+"seeeeeeeeeeeeeessssss");

        Integer userId = Integer.parseInt(userIdString);
        System.out.println(userId+"xxxxxxxxxxxxxxsssssss");

        userSessionMap.put(String.valueOf(userId), session);
        sessions.add(session);
        System.out.println("WebSocket opened: " + session.getId() + " with user ID: " + userId);
    }






    private static Integer getSessionUserId(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();

        // Imprime les attributs de la session WebSocket
        System.out.println("Attributs de la session WebSocket : " + attributes);

        // Vérifie si l'attribut "userId" est présent dans les attributs de la session
        if (attributes.containsKey("userId")) {
            System.out.println(attributes.containsKey("userId")+"IIIIIIIIIIIIII");

            // Récupère et retourne l'ID de l'utilisateur
            return (Integer) attributes.get("userId");
        } else {
            System.out.println("ooooooooooooooooooo");

            // Si l'attribut "userId" n'est pas présent, retourne null
            return null;
        }
    }



    public static Map<String, WebSocketSession> getUserSessions() {
        return userSessions;
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