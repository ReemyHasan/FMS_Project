package com.example.WebApplication.services;

import com.example.WebApplication.entities.EnrichedTrap;
import com.example.WebApplication.entities.RethinkChange;
import com.example.WebApplication.factory.RethinkDBConnectionFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RethinkDBService{
    private final Logger log = LoggerFactory.getLogger(RethinkDBService.class);
    private final RethinkDB r = RethinkDB.r;

    @Autowired
    public RethinkDBConnectionFactory connectionFactory;

//    @Autowired
//    private SocketTextHandler socket_server;

    public void saveKafkaMessageToRethink(EnrichedTrap rethinkTrap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(rethinkTrap);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            Map<String,EnrichedTrap> document = objectMapper.convertValue(jsonNode, Map.class);
//            System.out.println(jsonString);
//            System.out.println(jsonNode);
//            System.out.println(document);
//            System.out.println("I am here reem " + connectionFactory.getDbName() + " " + connectionFactory.getDbTableName());
            r.db(connectionFactory.getDbName()).table(connectionFactory.getDbTableName()).insert(document).run(connectionFactory.getConnection());
            System.out.println("I am here now reem");
        } catch (Exception e) {
            System.out.println("error " + e);
        }
    }

    public List<Map<String, Object> > getData() {
        try {
            Cursor<Map<String, Object>> cursor = r.db(connectionFactory.getDbName()).table(connectionFactory.getDbTableName()).run(connectionFactory.getConnection());
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
    public Cursor<RethinkChange> subscribe(){
        Connection connection = connectionFactory.getConnection();
        Cursor<RethinkChange> changeCursor = r.db(connectionFactory.getDbName()).table(connectionFactory.getDbTableName()).changes().optArg("include_initial",true).
                run(connection, RethinkChange.class);
        return changeCursor;
    }
    public void deleteById(String id){
        Connection connection = connectionFactory.getConnection();
        try{
            r.db(connectionFactory.getDbTableName()).table(connectionFactory.getDbTableName()).get(id).delete();
        }
        catch (Exception e){
            System.out.println("Couldn't Delete Data. Maybe the id is not there.");
        }
        return;
    }
    public Map<String,Object> getByID(String id){
        Connection connection = connectionFactory.getConnection();
        try{
            Map<String,Object> data = (Map<String, Object>) r.db(connectionFactory.getDbTableName()).table(connectionFactory.getDbTableName()).get(id);
            return data;
        }
        catch (Exception e){
            System.out.println("Couldn't Fetch Data. Maybe the id is not there.");
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

