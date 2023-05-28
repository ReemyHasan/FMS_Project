package com.example.consumer.configuration;

import com.example.consumer.services.RethinkDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SocketTextHandler socket_server;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("HEYYYYYYYYYYYYYYYYYY");
        registry.addHandler(socket_server, "/my-websocket").setAllowedOrigins("*");
    }
}
