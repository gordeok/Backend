package com.gordeok.idol.repository;

import com.gordeok.idol.entity.UserFavoriteIdol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteIdolRepository extends JpaRepository<UserFavoriteIdol, Long> {

    List<UserFavoriteIdol> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
