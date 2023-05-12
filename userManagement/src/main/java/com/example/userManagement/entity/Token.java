package com.example.userManagement.entity;

import com.example.userManagement.util.Indices;
import com.example.userManagement.util.TokenType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = Indices.TOKEN_INDEX)
@Setting(settingPath = "static/es-settings.json")
public class Token {

  @Id
  @GeneratedValue
  @Field(type = FieldType.Text)
  public String id;
  @Field(type = FieldType.Keyword)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;
  @Field(type = FieldType.Boolean)
  public boolean revoked;
  @Field(type = FieldType.Boolean)
  public boolean expired;
  @Field(type = FieldType.Text)
  public String user;
}
