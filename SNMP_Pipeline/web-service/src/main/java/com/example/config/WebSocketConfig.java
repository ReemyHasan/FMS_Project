//package com.example.WebServiceApplication.configuration;
//
//import com.example.WebServiceApplication.controller.SocketTextHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.configs.annotation.EnableWebSocket;
//import org.springframework.web.socket.configs.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.configs.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    @Autowired
//    private SocketTextHandler socket_server;
//    @Value("${websocket.path}")
//    private String webSocketPath;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
////        System.out.println(webSocketPath);
//        registry.addHandler(socket_server, webSocketPath).setAllowedOrigins("*");
//    }
//}
