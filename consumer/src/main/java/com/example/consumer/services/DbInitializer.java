package com.example.consumer.services;

import com.example.consumer.factory.RethinkDBConnectionFactory;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DbInitializer implements InitializingBean {
    @Autowired
    private RethinkDBConnectionFactory connectionFactory;

    @Autowired
    private RethinkDBService rethinkDBService;

    private static final RethinkDB r = RethinkDB.r;

    @Override
    public void afterPropertiesSet() throws Exception {
        createDb();

    }

    private void createDb() {
        Connection connection = connectionFactory.getConnection();
        List<String> dbList = r.dbList().run(connection);
        if (!dbList.contains("new_database")) {
            r.dbCreate("new_database").run(connection);
        }
        List<String> tables = r.db("new_database").tableList().run(connection);
        if (!tables.contains("new_table")) {
            r.db("new_database").tableCreate("new_table").run(connection);
            r.db("new_database").table("new_table").indexCreate("trap").run(connection);
        }
    }
}