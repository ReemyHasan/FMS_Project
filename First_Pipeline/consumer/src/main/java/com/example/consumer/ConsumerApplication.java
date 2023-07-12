package com.example.consumer;

import com.example.consumer.configuration.SocketTextHandler;
import com.example.consumer.factory.RethinkDBConnectionFactory;
import com.example.consumer.repository.RethinkChange;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
	private final RethinkDB r = RethinkDB.r;
	@Autowired
	public RethinkDBConnectionFactory connectionFactory;
	@Autowired
	private SocketTextHandler socket_server;

	@Override
	public void run(String... args) throws Exception {
		Connection connection = connectionFactory.getConnection();
		Cursor<RethinkChange> changeCursor = r.db("my_database").table("my_table").changes().optArg("include_initial",true).
				run(connection, RethinkChange.class);
		List<Map<String, Object>> result = new ArrayList<>();
		for (RethinkChange change : changeCursor){
			System.out.println("Something Changed");
			result.add(change.getNew_val());
			socket_server.broadcast(result);
		}
	}
//	@KafkaListener(topics = "Test")
//	public void handleNotification(String s) {
//
//		System.out.println("Received Message from Producer: "+s+"\n");
//	}

}
