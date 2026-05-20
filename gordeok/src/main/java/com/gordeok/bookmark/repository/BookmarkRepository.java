package com.gordeok.bookmark.repository;

import com.gordeok.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 유저의 북마크 목록
    List<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 특정 게시글 북마크 여부 확인
    Optional<Bookmark> findByUserIdAndPostId(Long userId, Long postId);

    // 북마크 존재 여부
    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
