package com.nival.chit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "MemberLoan")
public class MemberLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ChitGroup chitGroupId;

    private User userId;

    private Double principle;

    private Double interest;

    private Double tenure;

    private Double currentPayable;

    private Date startDate;

    private Date endDate;

    private String status;
}
