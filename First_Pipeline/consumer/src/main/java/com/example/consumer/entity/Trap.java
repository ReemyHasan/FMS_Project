package com.example.consumer.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@NoArgsConstructor
@Data
@Document(indexName = "traps")
public class Trap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String trap;

    public Trap(String trap) {
        this.trap = trap;
    }
}
