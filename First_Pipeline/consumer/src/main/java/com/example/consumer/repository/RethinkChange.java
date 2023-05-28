package com.example.consumer.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RethinkChange {
    private Map<String,Object> new_val;
    private Map<String,Object> old_val;
}
