//package com.example.kafkaConsumerSNMP.configuration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//@Component
//public class SocketTextHandler extends TextWebSocketHandler {
//    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        System.out.println("New connection from " + session.getRemoteAddress());
//        sessions.add(session);
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        System.out.println("Received message from " + session.getRemoteAddress() + ": " + message.getPayload());
//        // Handle incoming message
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        System.out.println("Connection closed to " + session.getRemoteAddress() + " with status " + status);
//        sessions.remove(session);
//    }
//
//    public void broadcast(List<Map<String, Object>> message) throws IOException {
//        System.out.println("Hey Broadcasting");
//        System.out.println(message);
//
//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String json = ow.writeValueAsString(message);
//        System.out.println(json);
//        TextMessage s = new TextMessage(json);
//        System.out.println(s);
//        for (WebSocketSession session : sessions) {
//            if (session.isOpen()) {
//                session.sendMessage(s);
//            }
//        }
//    }
//}
