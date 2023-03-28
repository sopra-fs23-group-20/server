package ch.uzh.ifi.hase.soprafs23.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/topic/{token}")
                .setAllowedOrigins("http://localhost:3000", "https://sopra-fs23-group-20-client.oa.r.appspot.com");
    }

    @Bean
    public MyHandler myHandler() {
        return new MyHandler();
    }
}
