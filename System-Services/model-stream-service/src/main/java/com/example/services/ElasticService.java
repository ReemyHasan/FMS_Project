package com.example.services;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.example.config.ElasticConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElasticService {

    @Autowired
    private ElasticConfig elConf;
    String dataStreamName = "model-data-stream";
    private List<JSONObject> bulk = new ArrayList<>();
    public void addDoc(JSONObject jsonObject){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = jsonObject.toString();

            Reader input = new StringReader(json);
            System.out.println(json);
            IndexRequest request = IndexRequest.of(i -> i
                    .index(dataStreamName)
                    .withJson(input)
            );
            IndexResponse response = null;
            response = elConf.getElasticClient().index(request);
            System.out.println("Indexed with version " + response.version());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public synchronized void addToBulk(JSONObject jsonObject){
        //bulk.add(jsonObject);
        addDoc(jsonObject);
    }
    public synchronized void sendBulk(){
        if (bulk.size() == 0) {
            //System.out.println("Nothing in the bulk");
            return;
        }
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (JSONObject s:bulk){
            br.operations(op -> op
                    .create(cr -> cr.index(dataStreamName).document(s))
            );
        }
        bulk.clear();
        BulkResponse result = null;
        try {
            BulkRequest bulkRequest = br.build();
            System.out.println(bulkRequest);
            System.out.println(bulkRequest.toString());
            result = elConf.getElasticClient().bulk(bulkRequest);
            System.out.println(result);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        System.out.println("Sent "+bulk.size());
    }
}
