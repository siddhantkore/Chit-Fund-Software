package com.nival.chit.repository;

import com.nival.chit.entity.ChitGroup;
import com.nival.chit.enums.ChitGroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ChitGroup entity.
 * Provides data access operations for chit groups.
 */
@Repository
public interface ChitGroupRepository extends JpaRepository<ChitGroup, Long> {
    /**
     * Find chit group by group code.
     *
     * @param groupCode the unique group code
     * @return optional chit group if found
     */
    Optional<ChitGroup> findByGroupCode(String groupCode);

    /**
     * Find chit groups by name (case-insensitive partial match).
     *
     * @param name the group name to search
     * @return list of matching chit groups
     */
    @Query("SELECT cg FROM ChitGroup cg WHERE LOWER(cg.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ChitGroup> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find chit groups by status.
     *
     * @param status the group status
     * @return list of chit groups with the specified status
     */
    List<ChitGroup> findByStatus(ChitGroupStatus status);
}
