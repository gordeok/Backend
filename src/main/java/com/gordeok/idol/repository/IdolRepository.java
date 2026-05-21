package com.gordeok.idol.repository;

import com.gordeok.idol.entity.Idol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdolRepository extends JpaRepository<Idol, Long> {

    // 이름으로 검색
    List<Idol> findByNameContainingOrderByNameAsc(String name);
}
