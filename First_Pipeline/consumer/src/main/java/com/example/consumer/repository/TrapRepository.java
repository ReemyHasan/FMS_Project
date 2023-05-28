package com.example.consumer.repository;


import com.example.consumer.entity.Trap;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrapRepository extends ElasticsearchRepository<Trap,Integer> {

}

