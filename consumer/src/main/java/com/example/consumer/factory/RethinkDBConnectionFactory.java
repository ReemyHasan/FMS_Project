package com.example.consumer.factory;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
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
            List<String> dbList = r.dbList().run(connection);
            if (!dbList.contains("my_database")) {
                System.out.println("Creating DATABASE Heeeeeeeeeeeeeeeeeeeeeeeeeere");
                r.dbCreate("my_database").run(connection);
            }
            List<String> tables = r.db("my_database").tableList().run(connection);
            if (!tables.contains("my_table")) {
                System.out.println("Creating Table Heeeeeeeeeeeeeeeeeeeeeeeeeere");
                r.db("my_database").tableCreate("my_table").run(connection);
                //r.db("my_database").table("my_table").indexCreate("trap").run(connection);
            }
        } catch (Exception e) {
            log.error("Error connecting to RethinkDB", e);
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

}

