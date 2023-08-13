package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "USER_APP",  uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String fname;

    private String lname;

    private String gender;

    private String country;

    private String email;

    private String password;

    @Temporal(TemporalType.DATE)
    private Date workingDate;

    private String role;
}
