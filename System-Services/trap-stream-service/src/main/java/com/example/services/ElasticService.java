package com.example.services;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.example.config.ElasticConfig;
import com.example.entities.ProcessedTrap;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class ElasticService {

    @Autowired
    private ElasticConfig elConf;

    private List<ProcessedTrap> bulk = new ArrayList<>();
    public void addDoc(ProcessedTrap processedTrap){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(processedTrap);

            Reader input = new StringReader(json);
            System.out.println(json);
            IndexRequest request = IndexRequest.of(i -> i
                    .index("traps-data-stream")
                    .withJson(input)
            );
            IndexResponse response = null;
            response = elConf.getElasticClient().index(request);
            System.out.println("Indexed with version " + response.version());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void addToBulk(ProcessedTrap processedTrap){
        bulk.add(processedTrap);
    }
    public void sendBulk(){
        if (bulk.size() == 0) {
            System.out.println("Nothing in the bulk");
            return;
        }
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (ProcessedTrap s:bulk){
            br.operations(op -> op
                    .create(cr -> cr.index("traps-data-stream").document(s))
            );
        }
        System.out.println("Sent "+bulk.size());
        bulk.clear();
        BulkResponse result = null;
        try {
            result = elConf.getElasticClient().bulk(br.build());
            System.out.println(result);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
    @PostConstruct
    public void magic(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                sendBulk();
            }
        };
        timer.schedule(task, 0, 3000);
    }
}
