package com.example.factory;

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

    @Value("${rethinkDBAbout}")
    private String dbAbout;

    @Value("${rethinkDBAboutTable}")
    private String dbTableAbout;

    @PostConstruct
    public Connection init() {
        try {
            connection = r.connection().hostname(host).port(port).connect();
            log.info("RethinkDB connected successfully");
            List<String> dbList = r.dbList().run(connection);
            if (!dbList.contains(dbName)) {
                System.out.println("Creating DATABASE");
                r.dbCreate(dbName).run(connection);
            }

            List<String> tables = r.db(dbName).tableList().run(connection);
            if (!tables.contains(dbTableName)) {
                System.out.println("Creating Table");
                r.db(dbName).tableCreate(dbTableName).run(connection);
            }
            if (!dbList.contains(dbAbout)) {
                System.out.println("Creating DATABASE");
                r.dbCreate(dbAbout).run(connection);
            }
            List<String> aboutTables = r.db(dbAbout).tableList().run(connection);

            if (!aboutTables.contains(dbTableAbout)) {
                System.out.println("Creating About Table");
                r.db(dbAbout).tableCreate(dbTableAbout).run(connection);
            }
        } catch (Exception e) {
            log.error("Error connecting to RethinkDB");
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

}

