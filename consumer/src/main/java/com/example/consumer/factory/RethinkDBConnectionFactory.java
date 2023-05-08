package com.example.consumer.factory;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Component
public class RethinkDBConnectionFactory {
    private final Logger log = LoggerFactory.getLogger(RethinkDBConnectionFactory.class);

    private final RethinkDB r = RethinkDB.r;
    private Connection connection;

    @Value("${rethinkdb.host}")
    private String host;

    @Value("${rethinkdb.port}")
    private int port;

    @PostConstruct
    public Connection init() {
        try {
            connection = r.connection().hostname(host).port(port).connect();
            log.info("RethinkDB connected successfully");
        } catch (Exception e) {
            log.error("Error connecting to RethinkDB", e);
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

}

