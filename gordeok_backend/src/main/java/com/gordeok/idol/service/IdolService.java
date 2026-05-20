package com.gordeok.idol.service;

import com.gordeok.idol.dto.IdolMemberResponseDto;
import com.gordeok.idol.dto.IdolResponseDto;
import com.gordeok.idol.entity.UserFavoriteIdol;
import com.gordeok.idol.entity.UserFavoriteMember;
import com.gordeok.idol.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdolService {

    private final IdolRepository idolRepository;
    private final IdolMemberRepository idolMemberRepository;
    private final UserFavoriteIdolRepository userFavoriteIdolRepository;
    private final UserFavoriteMemberRepository userFavoriteMemberRepository;

    // 아이돌 전체 목록 조회
    public List<IdolResponseDto> getIdolList() {
        return idolRepository.findAll().stream()
                .map(IdolResponseDto::new)
                .collect(Collectors.toList());
    }

    // 아이돌 검색
    public List<IdolResponseDto> searchIdols(String keyword) {
        return idolRepository.findByNameContainingOrderByNameAsc(keyword).stream()
                .map(IdolResponseDto::new)
                .collect(Collectors.toList());
    }

    // 특정 아이돌 멤버 목록 조회
    public List<IdolMemberResponseDto> getMembersByIdolId(Long idolId) {
        return idolMemberRepository.findByIdolId(idolId).stream()
                .map(IdolMemberResponseDto::new)
                .collect(Collectors.toList());
    }

    // 내 최애 그룹 목록 조회
    public List<IdolResponseDto> getMyFavoriteIdols(Long userId) {
        return userFavoriteIdolRepository.findByUserId(userId).stream()
                .map(fav -> idolRepository.findById(fav.getIdolId())
                        .map(IdolResponseDto::new)
                        .orElse(null))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    // 최애 그룹 저장 (기존 삭제 후 새로 저장)
    @Transactional
    public void updateFavoriteIdols(Long userId, List<Long> idolIds) {
        userFavoriteIdolRepository.deleteByUserId(userId);
        List<UserFavoriteIdol> favorites = idolIds.stream()
                .map(idolId -> new UserFavoriteIdol(userId, idolId))
                .collect(Collectors.toList());
        userFavoriteIdolRepository.saveAll(favorites);
    }

    // 내 최애 멤버 목록 조회
    public List<IdolMemberResponseDto> getMyFavoriteMembers(Long userId) {
        return userFavoriteMemberRepository.findByUserId(userId).stream()
                .map(fav -> idolMemberRepository.findById(fav.getMemberId())
                        .map(IdolMemberResponseDto::new)
                        .orElse(null))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    // 최애 멤버 저장 (기존 삭제 후 새로 저장)
    @Transactional
    public void updateFavoriteMembers(Long userId, List<Long> memberIds) {
        userFavoriteMemberRepository.deleteByUserId(userId);
        List<UserFavoriteMember> favorites = memberIds.stream()
                .map(memberId -> new UserFavoriteMember(userId, memberId))
                .collect(Collectors.toList());
        userFavoriteMemberRepository.saveAll(favorites);
    }
}
