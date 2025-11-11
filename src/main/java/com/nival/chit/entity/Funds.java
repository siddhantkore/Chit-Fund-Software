package com.nival.chit.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Funds")
@Data
public class Funds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double totalAmount;

    private int cyclePeriod;

    private Double interestRate;

    private ChitGroup chitGroupId;

    // updatedAt
}
