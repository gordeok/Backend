package com.gordeok.post.repository;

import com.gordeok.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 게시글 목록 최신순 조회 (페이징)
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 아이돌별 게시글 목록 조회
    Page<Post> findByIdolIdOrderByCreatedAtDesc(Long idolId, Pageable pageable);
}
