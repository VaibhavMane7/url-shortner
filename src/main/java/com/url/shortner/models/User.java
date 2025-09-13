package com.url.shortner.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String username;
    private String password;
    private  String role="ROLE_USER";



}
