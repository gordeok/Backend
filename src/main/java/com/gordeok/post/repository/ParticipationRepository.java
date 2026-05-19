package com.gordeok.post.repository;

import com.gordeok.post.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByMemberItemIdAndBuyerId(Long memberItemId, Long buyerId);
    Optional<Participation> findByMemberItemId(Long memberItemId);
}
