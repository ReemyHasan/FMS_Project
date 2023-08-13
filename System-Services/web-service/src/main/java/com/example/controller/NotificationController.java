package com.example.controller;

import com.example.handlers.RethinkAppChange;
import com.example.handlers.SseEmitters;
import com.example.services.RethinkDBService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@CrossOrigin("*")
@RequestMapping("/web/notifications")
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