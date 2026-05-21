package com.gordeok.idol.repository;

import com.gordeok.idol.entity.IdolMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdolMemberRepository extends JpaRepository<IdolMember, Long> {

    // 아이돌별 멤버 목록 조회
    List<IdolMember> findByIdolId(Long idolId);
}
