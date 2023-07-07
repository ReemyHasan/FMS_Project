package com.example.elasticJavaApi;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;
import com.example.elasticJavaApi.configuration.elasticConfig;
import com.example.elasticJavaApi.entities.ProcessedTrap;
import com.example.elasticJavaApi.entities.SeverityLevel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

@SpringBootApplication
public class ElasticJavaApiApplication {

	@Autowired
	private static elasticConfig service;
	public static void main(String[] args) {
		SpringApplication.run(ElasticJavaApiApplication.class, args);
	}

}
