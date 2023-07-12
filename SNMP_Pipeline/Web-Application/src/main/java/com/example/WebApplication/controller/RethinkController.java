//package com.example.WebApplication.controller;
//
//import com.example.WebApplication.controller.SocketTextHandler;
//import com.example.WebApplication.entities.RethinkChange;
//import com.example.WebApplication.services.RethinkDBService;
//import com.rethinkdb.net.Cursor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/rethink")
//public class RethinkController {
//
//    @Override
//    public void run(String... args) throws Exception {
//        Cursor<RethinkChange> changeCursor = rethinkDBService.subscribe();
//        System.out.println("I am Subscribing");
//        //List<RethinkChange> result = new ArrayList<>();
//        for (RethinkChange change : changeCursor){
////            System.out.println("Something Changed ");
////            System.out.println(change);
////            result.add(change);
//            try {
//                socket_server.broadcast(change);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//    }
//}
