package com.nival.chit.entity;

import com.nival.chit.enums.UserStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Membership")
@Data
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private User userid;

    private ChitGroup chitGroupId;

    private Date jointDate;

    private UserStatus status;

    /**
     *
     @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<Register> registers;

     @OneToMany(mappedBy = "events", cascade = CascadeType.ALL)
     private List<Results> results;

     */
}
