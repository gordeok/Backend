package com.gordeok.community.repository;

import com.gordeok.community.entity.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    // 전체 글 최신순
    Page<CommunityPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 전체 글 좋아요순
    Page<CommunityPost> findAllByOrderByLikeCountDescCreatedAtDesc(Pageable pageable);

    // 카테고리별 최신순
    Page<CommunityPost> findByCategoryOrderByCreatedAtDesc(
            String category,
            Pageable pageable
    );

    // 카테고리별 좋아요순
    Page<CommunityPost> findByCategoryOrderByLikeCountDescCreatedAtDesc(
            String category,
            Pageable pageable
    );
}