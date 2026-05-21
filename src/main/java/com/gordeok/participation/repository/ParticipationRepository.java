package com.gordeok.participation.repository;

import com.gordeok.participation.entity.Participation;
import com.gordeok.post.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByMemberItemId(Long memberItemId);

    Optional<Participation> findByMemberItem(MemberItem memberItem);

    // 특정 게시글(postId)에서 특정 구매자의 참여 정보 조회
    Optional<Participation> findByBuyerIdAndPostId(Long buyerId, Long postId);
}
