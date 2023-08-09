package com.example.WebApplication.controller;

//import com.example.WebApplication.controller.SocketTextHandler;
import com.example.WebApplication.entities.RethinkChange;
import com.example.WebApplication.services.RethinkDBService;
import com.rethinkdb.net.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/rethink")
public class RethinkController {
    @Autowired
    private RethinkDBService rethinkDBService;
    @PostMapping("/delete")
    public void delete(@RequestBody String id){
        rethinkDBService.deleteById(id);
    }
    @GetMapping("/data")
    public ResponseEntity<List<Map<String, Object>>> getData() {
        List<Map<String, Object>> result = rethinkDBService.getData();
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/severity-statistics")
    public ResponseEntity<Map<String, Map<String, Integer>>> getSeverityStatistics() {
        List<Map<String, Object>> rawResults = rethinkDBService.getData();
        if (rawResults != null) {
            Map<String, Map<String, Integer>> processedData = processRawResults(rawResults);

            return ResponseEntity.ok(processedData);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Map<String, Map<String, Integer>> processRawResults(List<Map<String, Object>> rawResults) {
        Map<String, Map<String, Integer>> processedData = new HashMap<>();

        for (Map<String, Object> rawEntry : rawResults) {
            String severityLevel = rawEntry.get("severity").toString();
            long timestamp = (long) rawEntry.get("timestamp"); // Assuming timestamp is a long value representing Unix timestamp in milliseconds

            // Convert Unix timestamp to a Date and extract hour
            String hour = convertTimestampToHour(timestamp);

            processedData.putIfAbsent(hour, new HashMap<>());

            Map<String, Integer> hourMap = processedData.get(hour);
            hourMap.put(severityLevel, hourMap.getOrDefault(severityLevel, 0) + 1);
        }

        return processedData;
    }

    private String convertTimestampToHour(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Customize the format as needed
        return sdf.format(date);
    }

    @GetMapping("/getTrapCount")
    public long getTrapCount() {
        try {
            return rethinkDBService.getTrapCount();
        }catch (Exception e){
            return 0;
        }
    }
    @GetMapping("/getErrorTrapCount")
    public long getErrorTrapCount() {
        try {
            return rethinkDBService.getErrorTrapCount();
        }catch (Exception e){
            return 0;
        }
    }

    @GetMapping("/getWarningTrapCount")
    public long getWarningTrapCount() {
        try {
            return rethinkDBService.getWarningTrapCount();
        }catch (Exception e){
            return 0;
        }
    }

    @GetMapping("/getInfoTrapCount")
    public long getInfoTrapCount() {
        try {
            return rethinkDBService.getInfoTrapCount();
        }catch (Exception e){
            return 0;
        }
    }
}
