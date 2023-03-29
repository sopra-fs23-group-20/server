package ch.uzh.ifi.hase.soprafs23.service;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIOConfig {

    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private Integer port;

    @Bean
    public Configuration socketIOConfiguration() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);

        config.setOrigin("http://localhost:3000");
        config.setOrigin("https://sopra-fs23-group-20-client.oa.r.appspot.com");

        return config;
    }

    @Bean
    public SocketIOServer socketIOServer(Configuration config) {
        return new SocketIOServer(config);
    }
}
