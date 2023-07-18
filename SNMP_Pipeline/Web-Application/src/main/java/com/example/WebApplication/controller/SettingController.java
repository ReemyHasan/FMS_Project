package com.example.WebApplication.controller;

import com.example.WebApplication.entities.AboutInfo;
import com.example.WebApplication.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/about")
public class SettingController {
    @Autowired
    private SettingService settingService;
    @DeleteMapping("/delete/{key}")
    public void delete(@PathVariable String key){
        settingService.deleteAboutInfo(key);
    }
    @GetMapping("/get/{key}")
    public ResponseEntity<AboutInfo> getByKey(@PathVariable String key) {
        AboutInfo result = settingService.getByKey(key);
        System.out.println(result);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/data")
    public ResponseEntity<List<Map<String, AboutInfo>>> getData() {
        List<Map<String, AboutInfo>> result = settingService.getData();
        System.out.println(result);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/post")
    public ResponseEntity<Boolean> postData(@RequestBody AboutInfo aboutInfo) {
        boolean result = settingService.putAboutInfo(aboutInfo);
        if (result == true) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/update")
    public ResponseEntity<Boolean> UpdateData(@RequestBody AboutInfo aboutInfo) {
        try {
            Boolean result = settingService.updateAboutInfo(aboutInfo);

            return ResponseEntity.ok(result);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/send-message")
    public ResponseEntity<Boolean> sendMessage(@RequestBody Map<String, String> message ) {
        try {
            String subject = message.get("subject");
            String body = message.get("body");
            String email = message.get("email");

            settingService.sendMessage(subject, body, email);
            return ResponseEntity.ok(true);
        }catch (Exception e){
            return ResponseEntity.ok(false);
        }
    }
}
