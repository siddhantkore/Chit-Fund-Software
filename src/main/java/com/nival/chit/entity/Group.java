package com.nival.chit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record Group (

        @Id
        int id,
        String name
) {
}
