package com.example.testingservice.controllers;

import com.example.testingservice.services.KafkaService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@RestController
@RequestMapping("/test")
public class TestingController {
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    KafkaService kafkaService;
    @GetMapping("/structure/1")
    public void go1() throws IOException {
        String s;
        for (Integer i=1;i<=11;i++){
            s = i.toString( );
            String csvFilePath = "raw data/raw CoreSwitch-I/CoreSwitch-I raw part"+s+".csv";
            Resource resource = resourceLoader.getResource("classpath:utils/"+csvFilePath);
            String filePath = resource.getFile().getAbsolutePath();
            csvFilePath = filePath;
            try (Reader reader = new FileReader(csvFilePath);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                for (CSVRecord csvRecord : csvParser) {
                    JSONObject jsonObject = new JSONObject(csvRecord.toMap());
                    kafkaService.send("Stream-Raw",jsonObject.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @GetMapping("/structure/2")
    public void go2() throws IOException {
        String s;
        for (Integer i=1;i<=11;i++){
            s = i.toString( );
            String csvFilePath = "raw data/raw CoreSwitch-II/CoreSwitch-II raw part"+s+".csv";
            Resource resource = resourceLoader.getResource("classpath:utils/"+csvFilePath);
            String filePath = resource.getFile().getAbsolutePath();
            csvFilePath = filePath;
            try (Reader reader = new FileReader(csvFilePath);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                for (CSVRecord csvRecord : csvParser) {
                    JSONObject jsonObject = new JSONObject(csvRecord.toMap());
                    kafkaService.send("Stream-Raw",jsonObject.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
