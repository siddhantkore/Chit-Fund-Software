package com.nival.chit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public record Member(
        @Id
        int id,

        String username,
        String name,

        @OneToMany
        Group group
) {
}
