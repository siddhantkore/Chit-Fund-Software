package com.nival.chit.repository;

import com.nival.chit.entity.Funds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FundsRepository extends JpaRepository<Funds, Long> {
}
