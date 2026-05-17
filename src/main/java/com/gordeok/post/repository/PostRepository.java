package com.gordeok.post.repository;

import com.gordeok.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 최신순
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 전체 오래된순
    Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

    // 제목 검색 최신순
    Page<Post> findByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);

    // 제목 검색 오래된순
    Page<Post> findByTitleContainingOrderByCreatedAtAsc(String title, Pageable pageable);

    // 아이돌별 최신순
    Page<Post> findByIdolIdOrderByCreatedAtDesc(Long idolId, Pageable pageable);

    // 아이돌별 오래된순
    Page<Post> findByIdolIdOrderByCreatedAtAsc(Long idolId, Pageable pageable);

    // 아이돌별 + 제목 검색 최신순
    Page<Post> findByIdolIdAndTitleContainingOrderByCreatedAtDesc(Long idolId, String title, Pageable pageable);

    // 아이돌별 + 제목 검색 오래된순
    Page<Post> findByIdolIdAndTitleContainingOrderByCreatedAtAsc(Long idolId, String title, Pageable pageable);
}
