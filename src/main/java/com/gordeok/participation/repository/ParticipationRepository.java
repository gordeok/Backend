package com.gordeok.participation.repository;

import com.gordeok.participation.entity.Participation;
import com.gordeok.post.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByMemberItemId(Long memberItemId);

    Optional<Participation> findByMemberItem(MemberItem memberItem);
}
