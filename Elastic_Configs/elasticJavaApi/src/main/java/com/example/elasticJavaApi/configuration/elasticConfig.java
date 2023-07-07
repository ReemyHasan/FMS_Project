package com.example.elasticJavaApi.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.cat.component_templates.ComponentTemplate;
import co.elastic.clients.elasticsearch.cluster.PutComponentTemplateRequest;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.ilm.IlmPolicy;
import co.elastic.clients.elasticsearch.ilm.Phase;
import co.elastic.clients.elasticsearch.ilm.Phases;
import co.elastic.clients.elasticsearch.ilm.PutLifecycleRequest;
import co.elastic.clients.elasticsearch.indices.CreateDataStreamRequest;
import co.elastic.clients.elasticsearch.indices.IndexState;
import co.elastic.clients.elasticsearch.indices.IndexTemplate;
import co.elastic.clients.elasticsearch.indices.PutIndexTemplateRequest;
import co.elastic.clients.elasticsearch.indices.put_index_template.IndexTemplateMapping;
import co.elastic.clients.elasticsearch.security.put_privileges.Actions;
import co.elastic.clients.elasticsearch.transform.Settings;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpDeserializer;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.elasticJavaApi.entities.ProcessedTrap;
import com.example.elasticJavaApi.entities.SeverityLevel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class elasticConfig {
    @Value("${workingPath}")
    private String workingPath;
    private RestClient restClient = RestClient.builder(
            new HttpHost("192.168.25.254", 9200)).build();

    // Create the transport with a Jackson mapper
    private ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // And create the API client
    private final ElasticsearchClient elasticClient = new ElasticsearchClient(transport);
    public void addPolicy(String path,String name){
        FileReader r = null;
        try {
            r = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        IlmPolicy plc = new IlmPolicy.Builder().withJson(r).build();
        PutLifecycleRequest policyReq = new PutLifecycleRequest.Builder().name(name).policy(plc).build();
        try {
            elasticClient.ilm().putLifecycle(policyReq);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addComponent(String path,String name){
        FileReader r = null;
        try {
            r = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        IndexState indexState = new IndexState.Builder().withJson(r).build();
        PutComponentTemplateRequest putComponentTemplateRequest = new PutComponentTemplateRequest
                .Builder()
                .name(name)
                .template(indexState).build();
        try {
            elasticClient.cluster().putComponentTemplate(putComponentTemplateRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addIndexTemplate(String path,String name){
        FileReader r = null;
        try {
            r = new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
//        IndexTemplate indexTemplate = new IndexTemplate.Builder().withJson(r).build();
//        IndexTemplateMapping indexTemplateMapping = new IndexTemplateMapping.Builder().withJson(r).build();
        PutIndexTemplateRequest putIndexTemplateRequest = new PutIndexTemplateRequest
                .Builder()
                .withJson(r)
                .name(name)
                .build();
        try {
            elasticClient.indices().putIndexTemplate(putIndexTemplateRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addDataStream(String name){
        CreateDataStreamRequest createDataStreamRequest = new CreateDataStreamRequest.Builder()
                .name(name).build();
        try {
            elasticClient.indices().createDataStream(createDataStreamRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PostConstruct
    public void ElasticDataStreamConfig() throws JsonProcessingException, FileNotFoundException {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory is: " + currentDirectory);
        addPolicy(workingPath+"/policy.json","Bashar");
        addComponent(workingPath+"/settings.json","bashar_setting");
        addComponent(workingPath+"/mappings.json","bashar_mapping");
        addIndexTemplate(workingPath+"/index_template.json","bashar_template");
        ProcessedTrap p = new ProcessedTrap("1.3.2.5.4.6.7","192.168.25.254",6,201,1569845288, SeverityLevel.ERROR, new ArrayList<>(), new GeoPoint(Math.random(),Math.random()));
        ObjectMapper ob = new ObjectMapper();
        //addDataStream("bashar-data-stream-3");
        try {
            String json = ob.writeValueAsString(p);
            System.out.println(json);
            String json1 = json;
            List<ProcessedTrap> jsons = new ArrayList<>();
            jsons.add(p);
            jsons.add(p);
            this.addDoc(jsons);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
    public void addDoc(List<ProcessedTrap> jsons){
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (ProcessedTrap s:jsons){
            br.operations(op -> op
                    .create(cr -> cr.index("bashar-data-stream-2").document(s))
            );
        }
        BulkResponse result = null;
        System.out.println("Hey I am here");
        try {
            System.out.println("Hey I am here");
            result = elasticClient.bulk(br.build());
            System.out.println(result);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        /*
        Reader input = new StringReader(json);
        IndexRequest<JsonData> request = IndexRequest.of(i -> i
                .index("bashar-data-stream-2")
                .withJson(input)
        );
        IndexResponse response = null;
        try {
            response = elasticClient.index(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Indexed with version " + response.version());*/
    }
}
