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
@Table(name = "Auction")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ChitGroup chitGroupId;

    private String month;

    private User winningMemberId;

    private Double winningAmount;

    private Double commission;

    private Date auctionDate;

    private String remark;
}
