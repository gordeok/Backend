package com.gordeok.community.repository;

import com.gordeok.community.entity.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    // 기존 홈화면용
    Page<CommunityPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<CommunityPost> findAllByOrderByLikeCountDescCreatedAtDesc(Pageable pageable);
    Page<CommunityPost> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);
    Page<CommunityPost> findByCategoryOrderByLikeCountDescCreatedAtDesc(String category, Pageable pageable);

    // 마이페이지: 내가 작성한 커뮤니티 글
    Page<CommunityPost> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
