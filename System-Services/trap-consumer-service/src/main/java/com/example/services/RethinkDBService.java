package com.example.services;

import com.example.entities.EnrichedTrap;
import com.example.factory.RethinkDBConnectionFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RethinkDBService {
    private final Logger log = LoggerFactory.getLogger(RethinkDBService.class);
    private final RethinkDB r = RethinkDB.r;

    @Autowired
    public RethinkDBConnectionFactory connectionFactory;

    public void saveKafkaMessageToRethink(EnrichedTrap rethinkTrap) {
        try {
            System.out.println(rethinkTrap.getDate());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(rethinkTrap);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            Map<String,EnrichedTrap> document = objectMapper.convertValue(jsonNode, Map.class);
            r.db(connectionFactory.getDbName()).table(connectionFactory.getDbTableName()).insert(document).run(connectionFactory.getConnection());
        } catch (Exception e) {
            System.out.println("error " + e);
        }
    }
    public List<Map<String, EnrichedTrap> > getData() {
        try {
            Cursor<Map<String, EnrichedTrap>> cursor = r.db(connectionFactory.getDbName()).table(connectionFactory.getDbTableName()).run(connectionFactory.getConnection());
            List<Map<String, EnrichedTrap>> result = new ArrayList<>();
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
            return result;
        } catch (Exception e) {
            log.error("Error getting data from RethinkDB", e);
            return null;
        }
    }
}

