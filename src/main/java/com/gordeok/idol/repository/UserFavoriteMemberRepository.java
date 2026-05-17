package com.gordeok.idol.repository;

import com.gordeok.idol.entity.UserFavoriteMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteMemberRepository extends JpaRepository<UserFavoriteMember, Long> {

    List<UserFavoriteMember> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
