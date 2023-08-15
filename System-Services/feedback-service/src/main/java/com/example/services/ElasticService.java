package com.example.services;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import com.example.configs.EasticConfig;
import com.example.dto.RangeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.io.*;
import java.util.*;
@Service
public class ElasticService {
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private EasticConfig elConf;
    String indexName = "knowledge-base";
    private List<JSONObject> bulk = new ArrayList<>();
    public String addInitialData(){
        Resource resource = resourceLoader.getResource("classpath:utils/"+"data_A1.csv");
        try {
            String filePath = resource.getFile().getAbsolutePath();
            Reader reader = new FileReader(filePath);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : csvParser) {
                JSONObject jsonObject = new JSONObject(csvRecord.toMap());
                this.addToBulk(jsonObject);
            }
            return "Added Successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Nothing Added";
        }
    }

    public void addDoc(JSONObject jsonObject){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = jsonObject.toString();

            Reader input = new StringReader(json);
            System.out.println(json);
            IndexRequest request = IndexRequest.of(i -> i
                    .index(indexName)
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
        bulk.add(jsonObject);
        //addDoc(jsonObject);
    }
    public synchronized void sendBulk(){
        if (bulk.size() == 0) {
//            System.out.println("Nothing in the bulk");
            return;
        }
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (JSONObject s:bulk){
            br.operations(op -> op
                    .create(cr -> cr.index(indexName).document(s.toMap()))
            );
        }
        int sz = bulk.size();
        bulk.clear();
        BulkResponse result = null;
        try {
            BulkRequest bulkRequest = br.build();
            result = elConf.getElasticClient().bulk(bulkRequest);
            System.out.println(result);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public void updateIndex(RangeRequest rangeRequest, String indexNameSearch){
        List<Map<String,Object> > docs = searchRange(rangeRequest,"model-data-stream");
        JSONObject jsonObject;
        for (Map<String,Object> w:docs){
            w.remove("@timestamp");
            w.put("class",rangeRequest.getClas());
            jsonObject = new JSONObject(w);
            addToBulk(jsonObject);
        }
    }
    public List<Map<String, Object>> searchRange(RangeRequest rangeRequest, String indexNameSearch){
        Query byRangeTime = RangeQuery.of(r -> r
                .field("@timestamp")
                .gte(JsonData.of(rangeRequest.getStart()))
                .lte(JsonData.of(rangeRequest.getEnd()))
                .format("strict_date_optional_time||epoch_second")
                )._toQuery();
        Class<Map<String, Object>> tDocumentClass = (Class<Map<String, Object>>) (Class<?>) Map.class;
        try {
            SearchResponse<Map <String,Object> > response = elConf.getElasticClient().search(s -> s
                            .index(indexNameSearch)
                            .query(q -> q
                                    .bool(b -> b
                                            .must(byRangeTime)
                                    )
                            ),tDocumentClass
            );
            TotalHits total = response.hits().total();
            System.out.println("Total: "+total.value());
            List<Hit<Map<String,Object>>> hits = response.hits().hits();
            List<Map<String,Object>> ret = new ArrayList<>();
            for (Hit<Map<String,Object>> hit: hits) {
                Map<String,Object> doc = hit.source();
                ret.add(doc);
            }
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   public void deleteAll(){
        String s = "{\"match_all\": {}}";
       InputStream inputStream = new ByteArrayInputStream(s.getBytes());
       System.out.println(s);

       System.out.println(inputStream);
       Query deleteAllDocs = Query.of(r->r
               .withJson(inputStream));
       System.out.println(deleteAllDocs);

       DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest.Builder().index(indexName).query(deleteAllDocs).build();
       System.out.println(deleteByQueryRequest);

       try {
           elConf.getElasticClient().deleteByQuery(deleteByQueryRequest);
       } catch (IOException e) {
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
