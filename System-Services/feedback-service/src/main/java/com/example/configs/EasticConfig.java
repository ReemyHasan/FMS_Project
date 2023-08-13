package com.example.configs;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.PutComponentTemplateRequest;
import co.elastic.clients.elasticsearch.ilm.IlmPolicy;
import co.elastic.clients.elasticsearch.ilm.PutLifecycleRequest;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Component
@Getter
public class EasticConfig {
    @Autowired
    private ResourceLoader resourceLoader;

    @Value(("${elasticsearch.hostname}"))
    private String hostName;

    @Value(("${elasticsearch.port}"))
    private int port;

    private RestClient restClient;
    private  ElasticsearchTransport transport;
    private ElasticsearchClient elasticClient;

    public void addPolicy(String path,String name) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:utils/"+path);
        System.out.println(resource);
        String filePath = resource.getFile().getAbsolutePath();
        System.out.println(filePath);
        path = filePath;
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
    public void addComponent(String path,String name) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:utils/"+path);
        String filePath = resource.getFile().getAbsolutePath();
        path = filePath;
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
    public void addIndexTemplate(String path,String name) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:utils/"+path);
        String filePath = resource.getFile().getAbsolutePath();
        path = filePath;
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
        ExistsRequest existsRequest = new ExistsRequest.Builder().index("model-data-stream").build();
        try {
            BooleanResponse b = elasticClient.indices().exists(existsRequest);
            if (b.value() == false)
                elasticClient.indices().createDataStream(createDataStreamRequest);
        } catch (Exception e) {
            return;
//            throw new RuntimeException(e);
        }
    }
    public void addIndex(String name){
        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
                .index(name).build();
        ExistsRequest existsRequest = new ExistsRequest.Builder().index(name).build();
        try {
            BooleanResponse b = elasticClient.indices().exists(existsRequest);
            if (b.value() == false)
                elasticClient.indices().create(createIndexRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostConstruct
    public void ElasticDataStreamConfig() throws JsonProcessingException, FileNotFoundException {
        restClient = RestClient.builder(
                new HttpHost(hostName,port)).build();
        transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        elasticClient = new ElasticsearchClient(transport);
        try {
            //addPolicy("policy.json","knowledge-base-policy");
            addComponent("settings.json","knowledge-base-setting");
            addComponent("mappings.json","knowledge-base-mapping");
            addIndexTemplate("index_template.json","knowledge-base-template");
            addIndex("knowledge-base");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        //Bulk Schedule Send
    }

}
