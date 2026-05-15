package com.gordeok.post.repository;

import com.gordeok.post.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {

    // 게시글 id로 멤버 아이템 목록 조회
    List<MemberItem> findByPostId(Long postId);
}
