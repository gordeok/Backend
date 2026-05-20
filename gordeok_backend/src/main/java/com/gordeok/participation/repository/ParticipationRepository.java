package com.gordeok.participation.repository;

import com.gordeok.participation.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByMemberItemId(Long memberItemId);
}