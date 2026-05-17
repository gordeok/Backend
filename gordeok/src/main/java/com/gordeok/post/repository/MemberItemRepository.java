package com.gordeok.post.repository;

import com.gordeok.post.entity.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {
}