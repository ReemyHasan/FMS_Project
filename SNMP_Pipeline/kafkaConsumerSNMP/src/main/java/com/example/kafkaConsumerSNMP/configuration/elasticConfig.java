package com.example.kafkaConsumerSNMP.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.PutComponentTemplateRequest;
import co.elastic.clients.elasticsearch.ilm.IlmPolicy;
import co.elastic.clients.elasticsearch.ilm.PutLifecycleRequest;
import co.elastic.clients.elasticsearch.indices.CreateDataStreamRequest;
import co.elastic.clients.elasticsearch.indices.IndexState;
import co.elastic.clients.elasticsearch.indices.PutIndexTemplateRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Configuration
public class elasticConfig {
    /*private RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200)).build();

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
        addPolicy("src/main/resources/utils/policy.json","Bashar");
        addComponent("src/main/resources/utils/settings.json","bashar_setting");
        addComponent("src/main/resources/utils/mappings.json","bashar_mapping");
        addIndexTemplate("src/main/resources/utils/index_template.json","bashar_template");
        addDataStream("bashar-data-stream-2");
    }
    public void addDocToDataStream(JsonNode trap){
        elasticClient.indices();
    }*/
}
