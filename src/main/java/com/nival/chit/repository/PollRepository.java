package com.nival.chit.repository;

import com.nival.chit.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByChitGroupIdOrderByCreatedAtDesc(Long groupId);
}
