package com.teamforge.teammatch.repository;

import com.teamforge.teammatch.model.TeamMemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, String> {
    List<TeamMemberEntity> findAllByOrderByNameAsc();
}