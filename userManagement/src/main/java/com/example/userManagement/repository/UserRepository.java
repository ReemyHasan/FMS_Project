package com.example.userManagement.repository;



import com.example.userManagement.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ElasticsearchRepository<User,Integer> {
    User findByUsername(String username);
}