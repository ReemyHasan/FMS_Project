package com.example.WebApplication.services;

import com.example.WebApplication.entities.AboutInfo;
import com.example.WebApplication.factory.RethinkDBConnectionFactory;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SettingService {
    @Autowired
    private JavaMailSender mailSender;
    private final Logger log = LoggerFactory.getLogger(RethinkDBService.class);
    private final RethinkDB r = RethinkDB.r;

    @Autowired
    public RethinkDBConnectionFactory connectionFactory;

    public List<Map<String, AboutInfo>> getData() {
        try {
            Cursor<Map<String, AboutInfo>> cursor = r.db(connectionFactory.getDbAbout()).table(connectionFactory.getDbTableAbout()).run(connectionFactory.getConnection());
            List<Map<String, AboutInfo>> result = new ArrayList<>();
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
            return result;
        } catch (Exception e) {
            log.error("Error getting data from RethinkDB", e);
            return null;
        }
    }
    public AboutInfo getByKey(String key) {
        try {
            Cursor<Map<String, AboutInfo>> cursor = r.db(connectionFactory.getDbAbout())
                    .table(connectionFactory.getDbTableAbout())
                    .filter(row -> row.g("key").eq(key))
                    .run(connectionFactory.getConnection());

            if (cursor.hasNext()) {
                return cursor.next().get("value");
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error getting data from RethinkDB", e);
            return null;
        }
    }
    public boolean putAboutInfo(AboutInfo aboutInfo) {
        try {
            r.db(connectionFactory.getDbAbout())
                    .table(connectionFactory.getDbTableAbout())
                    .insert(r.hashMap("key", aboutInfo.getKey())
                            .with("value", aboutInfo.getValue()))
                    .run(connectionFactory.getConnection());
            return true;
        } catch (Exception e) {
            log.error("Error saving data to RethinkDB", e);
            return false;
        }
    }

    public boolean updateAboutInfo(AboutInfo aboutInfo) {
        try {
            r.db(connectionFactory.getDbAbout())
                    .table(connectionFactory.getDbTableAbout())
                    .filter(row -> row.g("key").eq(aboutInfo.getKey()))
                    .update(r.hashMap("value", aboutInfo.getValue()))
                    .run(connectionFactory.getConnection());
            return true;
        } catch (Exception e) {
            log.error("Error updating data in RethinkDB", e);
            return false;
        }
    }
    public void deleteAboutInfo(String key) {
        try {
            r.db(connectionFactory.getDbAbout())
                    .table(connectionFactory.getDbTableAbout())
                    .filter(row -> row.g("key").eq(key))
                    .delete()
                    .run(connectionFactory.getConnection());
        } catch (Exception e) {
            log.error("Error deleting data from RethinkDB", e);
        }
    }
    public void sendMessage(String subject, String body, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("projectfms6@gmail.com");
        message.setTo("projectfms6@gmail.com");
        message.setText(email+"\n"+body);
        message.setSubject("FeedBack from: "+subject);
        mailSender.send(message);
        System.out.println("Mail Send...");
    }
}
