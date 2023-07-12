package com.example.WebApplication.factory;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class RethinkDBConnectionFactory {
    private final Logger log = LoggerFactory.getLogger(RethinkDBConnectionFactory.class);

    private final RethinkDB r = RethinkDB.r;
    private Connection connection;

    @Value("${rethinkdb.host}")
    private String host;

    @Value("${rethinkdb.port}")
    private int port;

    @Value("${rethinkDBName}")
    private String dbName;

    @Value("${rethinkDBTableName}")
    private String dbTableName;

    @PostConstruct
    public Connection init() {
        try {
            connection = r.connection().hostname(host).port(port).connect();
            log.info("RethinkDB connected successfully");
            List<String> dbList = r.dbList().run(connection);
            if (!dbList.contains(dbName)) {
//                System.out.println("Creating DATABASE Heeeeeeeeeeeeeeeeeeeeeeeeeere");
                r.dbCreate(dbName).run(connection);
            }
            List<String> tables = r.db(dbName).tableList().run(connection);
            if (!tables.contains(dbTableName)) {
//                System.out.println("Creating Table Heeeeeeeeeeeeeeeeeeeeeeeeeere");
                r.db(dbName).tableCreate(dbTableName).run(connection);
                //r.db(dbName).table(dbTableName).indexCreate("timestamp").run(connection);
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

