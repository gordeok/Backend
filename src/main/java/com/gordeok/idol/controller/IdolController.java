package com.gordeok.idol.controller;

import com.gordeok.idol.dto.FavoriteUpdateRequestDto;
import com.gordeok.idol.dto.IdolMemberResponseDto;
import com.gordeok.idol.dto.IdolResponseDto;
import com.gordeok.idol.service.IdolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IdolController {

    private final IdolService idolService;

    // 아이돌 목록 조회 (검색 포함)
    // GET /api/idols?keyword=검색어
    @GetMapping("/api/idols")
    public ResponseEntity<List<IdolResponseDto>> getIdolList(
            @RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return ResponseEntity.ok(idolService.searchIdols(keyword));
        }
        return ResponseEntity.ok(idolService.getIdolList());
    }

    // 특정 아이돌 멤버 목록 조회
    // GET /api/idols/{idolId}/members
    @GetMapping("/api/idols/{idolId}/members")
    public ResponseEntity<List<IdolMemberResponseDto>> getMembersByIdolId(
            @PathVariable Long idolId) {
        return ResponseEntity.ok(idolService.getMembersByIdolId(idolId));
    }

    // 내 최애 그룹 목록 조회
    // GET /api/users/me/favorite-idols?userId=1
    @GetMapping("/api/users/me/favorite-idols")
    public ResponseEntity<List<IdolResponseDto>> getMyFavoriteIdols(
            @RequestParam Long userId) {
        return ResponseEntity.ok(idolService.getMyFavoriteIdols(userId));
    }

    // 최애 그룹 저장/수정
    // POST /api/users/me/favorite-idols?userId=1
    @PostMapping("/api/users/me/favorite-idols")
    public ResponseEntity<Void> updateFavoriteIdols(
            @RequestParam Long userId,
            @RequestBody FavoriteUpdateRequestDto request) {
        idolService.updateFavoriteIdols(userId, request.getIdolIds());
        return ResponseEntity.ok().build();
    }

    // 내 최애 멤버 목록 조회
    // GET /api/users/me/favorite-members?userId=1
    @GetMapping("/api/users/me/favorite-members")
    public ResponseEntity<List<IdolMemberResponseDto>> getMyFavoriteMembers(
            @RequestParam Long userId) {
        return ResponseEntity.ok(idolService.getMyFavoriteMembers(userId));
    }

    // 최애 멤버 저장/수정
    // POST /api/users/me/favorite-members?userId=1
    @PostMapping("/api/users/me/favorite-members")
    public ResponseEntity<Void> updateFavoriteMembers(
            @RequestParam Long userId,
            @RequestBody FavoriteUpdateRequestDto request) {
        idolService.updateFavoriteMembers(userId, request.getMemberIds());
        return ResponseEntity.ok().build();
    }
}
