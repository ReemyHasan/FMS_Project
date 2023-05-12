package com.example.userManagement.repository;


import com.example.userManagement.entity.Role;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends ElasticsearchRepository<Role,Integer> {

}
