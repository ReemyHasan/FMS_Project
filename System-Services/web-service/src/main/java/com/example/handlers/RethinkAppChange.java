package com.example.handlers;

import com.example.entities.RethinkChange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

@Getter
@Setter
public class RethinkAppChange extends ApplicationEvent {
    private RethinkChange rethinkChange;
    public RethinkAppChange(Object source,Map<String,Object> oldVal,Map<String,Object> newVal)
    {
        super(source);
        this.rethinkChange = new RethinkChange(newVal,oldVal);
    }
}
