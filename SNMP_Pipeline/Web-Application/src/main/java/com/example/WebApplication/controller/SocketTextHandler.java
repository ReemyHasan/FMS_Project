package com.example.WebApplication.controller;

import com.example.WebApplication.entities.EnrichedTrap;
import com.example.WebApplication.entities.RethinkChange;
import com.example.WebApplication.services.RethinkDBService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rethinkdb.net.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RestController
@RequestMapping("/api/rethink")
public class SocketTextHandler extends TextWebSocketHandler implements CommandLineRunner {
    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Autowired
    private RethinkDBService rethinkDBService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New connection from " + session.getRemoteAddress());
        sessions.add(session);
        this.sendAllToSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message from " + session.getRemoteAddress() + ": " + message.getPayload());
        // Handle incoming message
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed to " + session.getRemoteAddress() + " with status " + status);
        sessions.remove(session);
    }

    public void broadcast(Object message) throws IOException {
        System.out.println("Hey Broadcasting");
        System.out.println(message);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(message);
        System.out.println(json);
        TextMessage s = new TextMessage(json);
        //System.out.println(s);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(s);
            }
        }
    }
    public void sendMessageToSession(Object message,WebSocketSession session) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(json);
        TextMessage s = new TextMessage(json);
        if (session.isOpen()) {
            try {
                session.sendMessage(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendAllToSession(WebSocketSession session){
        List<Map<String, Object>> data = rethinkDBService.getData();
        RethinkChange change = new RethinkChange();
        change.setOld_val(null);
        for (Map<String, Object> doc: data){
            change.setNew_val(doc);
            this.sendMessageToSession(change,session);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        Cursor<RethinkChange> changeCursor = rethinkDBService.subscribe();
        System.out.println("I am Subscribing");
        System.out.println(changeCursor.getClass());
        System.out.println(changeCursor);
        //List<RethinkChange> result = new ArrayList<>();
        for (RethinkChange change : changeCursor){
            System.out.println("Something Changed ");
//            result.add(change);
            try {
                this.broadcast(change);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PostMapping("/delete")
    public void delete(@RequestBody String id){
        rethinkDBService.deleteById(id);
    }
}
