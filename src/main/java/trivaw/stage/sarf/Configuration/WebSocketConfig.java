 package trivaw.stage.sarf.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

 @Configuration
 @EnableWebSocket
 public class WebSocketConfig implements WebSocketConfigurer {
     @Override
     public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
         registry.addHandler(myWebSocketEndpoint(), "/ws").setAllowedOrigins("http://localhost:4200");
     }

     @Bean
     public WebSocketEndpoint myWebSocketEndpoint() {
         return new WebSocketEndpoint();
     }

 }