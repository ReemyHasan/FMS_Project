package com.example.WebApplication.services;

import com.example.WebApplication.entities.EnrichedTrap;
import com.example.WebApplication.entities.RethinkChange;
import com.example.WebApplication.factory.RethinkDBConnectionFactory;
import com.example.WebApplication.handlers.RethinkAppChange;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class RethinkDBService{
    private final Logger log = LoggerFactory.getLogger(RethinkDBService.class);
    private final RethinkDB r = RethinkDB.r;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RethinkDBConnectionFactory connectionFactory;

    @Bean
    ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        eventMulticaster.setErrorHandler(TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER);
        return eventMulticaster;
    }
    public void saveKafkaMessageToRethink(EnrichedTrap rethinkTrap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(rethinkTrap);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            Map<String,EnrichedTrap> document = objectMapper.convertValue(jsonNode, Map.class);
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
    public void subscribe() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(() -> {
            try {
                Connection connection = connectionFactory.getConnection();
                Cursor<RethinkChange> changeCursor = r.db(connectionFactory.getDbName()).table(connectionFactory.getDbTableName()).changes().optArg("include_initial", true).
                        run(connection, RethinkChange.class);
                while (changeCursor.hasNext()) {
                    RethinkChange changedData = changeCursor.next();
                    eventPublisher.publishEvent(new RethinkAppChange(this, changedData.getOld_val(), changedData.getNew_val()));
                }
            } catch (Exception e) {
                System.out.println("Bkh");
            }});
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
}

