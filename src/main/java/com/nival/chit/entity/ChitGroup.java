package com.nival.chit.entity;

import com.nival.chit.enums.ChitGroupStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.sql.Date;

@Entity
@Data
@Table(name = "ChitGroup")
public class ChitGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String groupCode;

    private String name;

    private int duration;

    private double monthlyAmount;

    private int totalMembers;

    private Date startDate;

    private ChitGroupStatus status;

    @OneToOne
    private Funds fundId;

    // createdAt
}
