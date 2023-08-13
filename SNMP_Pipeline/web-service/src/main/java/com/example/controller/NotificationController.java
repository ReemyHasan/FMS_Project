package com.example.WebApplication.controller;

import com.example.WebApplication.handlers.RethinkAppChange;
import com.example.WebApplication.handlers.SseEmitters;
import com.example.WebApplication.services.RethinkDBService;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.example.WebApplication.entities.RethinkChange;
import com.rethinkdb.net.Cursor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/notifications")
public class NotificationController implements ApplicationListener<RethinkAppChange> {
    @Autowired
    private RethinkDBService rethinkDBService;
    private final SseEmitters emitters = new SseEmitters();

    @PostConstruct
    void init() {
        rethinkDBService.subscribe();
    }

    @GetMapping("/sub")
    public SseEmitter emitData() {
        return emitters.add(new SseEmitter(Long.MAX_VALUE));
    }

    @Override
    public void onApplicationEvent(RethinkAppChange event) {
        emitters.send(event.getRethinkChange());
    }
}