package com.gordeok.participation.repository;

import com.gordeok.participation.entity.Participation;
import com.gordeok.post.entity.MemberItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByMemberItemId(Long memberItemId);

    Optional<Participation> findByMemberItem(MemberItem memberItem);

    Optional<Participation> findByBuyerIdAndPostId(Long buyerId, Long postId);

    // 구매 목록: Post.status 기준 페이징
    @Query("SELECT p FROM Participation p " +
           "JOIN FETCH p.post post " +
           "JOIN FETCH p.memberItem mi " +
           "WHERE p.buyer.id = :buyerId AND post.status = :postStatus " +
           "ORDER BY p.createdAt DESC")
    Page<Participation> findByBuyerIdAndPostStatus(
            @Param("buyerId") Long buyerId,
            @Param("postStatus") String postStatus,
            Pageable pageable);

    // 동일 postId 에 대한 내 모든 참여 (후기 필터용)
    List<Participation> findByBuyerIdAndPostId(Long buyerId, Long postId);

    // 신뢰 점수 산정용
    long countByBuyerId(Long buyerId);

    @Query("SELECT COUNT(p) FROM Participation p WHERE p.buyer.id = :buyerId AND p.post.status = :postStatus")
    long countByBuyerIdAndPostStatus(@Param("buyerId") Long buyerId, @Param("postStatus") String postStatus);
}
