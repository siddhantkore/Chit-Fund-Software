package com.nival.chit.entity;

import com.nival.chit.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "username", unique = true, nullable = false, length = 15)
    private String username;

    @Column(length = 10)
    private Long phone;

    @Column(unique = true, nullable = false, length = 20)
    private String email;

    @Column(nullable = false, length = 30)
    private String password;


    private UserRoles role;

    // created at
    // updated at

}