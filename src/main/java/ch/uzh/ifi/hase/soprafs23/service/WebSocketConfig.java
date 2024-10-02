package ch.uzh.ifi.hase.soprafs23.service;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic");
    }

    @Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    String allowedOriginsEnv = System.getenv("ALLOWED_ORIGINS");
    String[] allowedOrigins = (allowedOriginsEnv != null && !allowedOriginsEnv.isEmpty())
            ? (allowedOriginsEnv + ",http://localhost:3000").split(",")
            : new String[] { "http://localhost:3000" };

    registry.addEndpoint("/socket")
            .setAllowedOrigins(allowedOrigins)
            .withSockJS();
}


}
