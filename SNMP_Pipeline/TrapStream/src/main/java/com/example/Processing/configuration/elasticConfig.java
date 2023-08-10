package com.example.Processing.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.PutComponentTemplateRequest;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.ilm.IlmPolicy;
import co.elastic.clients.elasticsearch.ilm.PutLifecycleRequest;
import co.elastic.clients.elasticsearch.indices.CreateDataStreamRequest;
import co.elastic.clients.elasticsearch.indices.IndexState;
import co.elastic.clients.elasticsearch.indices.PutIndexTemplateRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.Processing.entities.ProcessedTrap;
import com.example.Processing.entities.SeverityLevel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Getter
public class elasticConfig {
    @Value("${workingPath}")
    private String workingPath;
    private RestClient restClient = RestClient.builder(
            new HttpHost("172.29.3.220", 9200)).build();

    private  ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    private ElasticsearchClient elasticClient = new ElasticsearchClient(transport);
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
        } catch (Exception e) {
            return;
//            throw new RuntimeException(e);
        }
    }
    @PostConstruct
    public void ElasticDataStreamConfig() throws JsonProcessingException, FileNotFoundException {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Current working directory is: " + currentDirectory);
        addPolicy(workingPath+"/policy.json","Traps_Policy");
        addComponent(workingPath+"/settings.json","traps_setting");
        addComponent(workingPath+"/mappings.json","traps_mapping");
        addIndexTemplate(workingPath+"/index_template.json","traps_template");
        addDataStream("traps-data-stream");
        //Bulk Schedule Send

    }
    public void addDoc(List<ProcessedTrap> jsons){
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (ProcessedTrap s:jsons){
            br.operations(op -> op
                    .create(cr -> cr.index("traps-data-stream").document(s))
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

    }
}
