package com.example.consumer.services;

import com.example.consumer.configuration.SocketTextHandler;
import com.example.consumer.factory.RethinkDBConnectionFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Json;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.net.Cursor;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.rethinkdb.net.Connection;
import com.rethinkdb.model.MapObject;
import org.springframework.web.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class RethinkDBService {
    private final Logger log = LoggerFactory.getLogger(RethinkDBService.class);
    private final RethinkDB r = RethinkDB.r;
    @Autowired
    public RethinkDBConnectionFactory connectionFactory;

    public void saveKafkaMessageToRethink(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonString = "{\"trap\":\" "+message+"\"}";
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            // Create a RethinkDB document object using the parsed JSON object

            Map<String, Object> document = objectMapper.convertValue(jsonNode, Map.class);

            r.db("my_database").table("my_table").insert(document).run(connectionFactory.getConnection());
        } catch (IOException e) {

            System.out.println("error " + e);
        }
    }
    public void saveData(String database, String table, Map<String, Object> data) {
        try {
            r.db(database).table(table).insert(data).run(connectionFactory.getConnection());
            log.info("Data saved successfully in RethinkDB");
        } catch (Exception e) {
            log.error("Error saving data in RethinkDB", e);
        }
    }
    public List<Map<String, Object>> getData(String database, String table) {
        try {
            Cursor<Map<String, Object>> cursor = r.db(database).table(table).run(connectionFactory.getConnection());
            List<Map<String, Object>> result = new ArrayList<>();
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
            return result;
        } catch (Exception e) {
            log.error("Error getting data from RethinkDB", e);
            return null;
        }
    }
//Connection conn = connectionFactory.getConnection();
    //Cursor<Map<String, Object> > changeCursor = r.db(database).table(table).changes().run(conn);

        /*for (Object change : changeCursor) {
            result.add((Map<String, Object>) change);
        }
        try {
            socket_server.broadcast(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

}

