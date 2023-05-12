package com.example.userManagement.repository;

import java.util.List;
import java.util.Optional;

import com.example.userManagement.entity.Token;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TokenRepository extends ElasticsearchRepository<Token, Integer> {

  List<Token> findAllValidTokenByUser(String id);
  Token findByUser(String user);
  Optional<Token> findByToken(String token);
}
