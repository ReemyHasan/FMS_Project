package com.example.WebApplication.entities;

import lombok.*;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RethinkChange{
    private Map<String,Object> new_val;
    private Map<String,Object> old_val;

}
