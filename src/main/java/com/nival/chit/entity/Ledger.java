package com.nival.chit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@Table(name = "Ledger")
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private ChitGroup chitGroupId;

    private String entryType;

    private Double amount;

    private Date entryDate;

    private String remark;

    private String referenceId; // link to Auction or payments

    private User createdBy;

}
