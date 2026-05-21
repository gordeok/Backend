package com.gordeok.community.repository;

import com.gordeok.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 목록 조회
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // 특정 게시글의 댓글 개수 조회
    int countByPostId(Long postId);
}