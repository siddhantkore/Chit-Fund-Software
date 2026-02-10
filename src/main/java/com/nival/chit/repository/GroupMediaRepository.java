package com.nival.chit.repository;

import com.nival.chit.entity.GroupMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMediaRepository extends JpaRepository<GroupMedia, Long> {
    List<GroupMedia> findByChitGroupIdOrderByCreatedAtDesc(Long groupId);
}
