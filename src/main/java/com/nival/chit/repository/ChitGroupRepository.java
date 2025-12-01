package com.nival.chit.repository;

import com.nival.chit.entity.ChitGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChitGroupRepository extends JpaRepository<ChitGroup, Long> {
}
