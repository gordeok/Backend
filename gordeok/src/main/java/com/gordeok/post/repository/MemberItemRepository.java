package com.gordeok.post.repository;

import com.gordeok.post.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {
    List<MemberItem> findByPostId(Long postId);
}