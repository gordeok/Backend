package com.gordeok.user.controller;

import com.gordeok.global.storage.FileStorageService;
import com.gordeok.user.dto.*;
import com.gordeok.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    // ── MY-01: 내 프로필 조회 ──
    @GetMapping("/me")
    public ResponseEntity<MyProfileResponseDto> getMyProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

    // ── MY-02: 타 유저 프로필 조회 ──
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    // ── MY-03: 닉네임 수정 ──
    @PatchMapping("/me/nickname")
    public ResponseEntity<UpdateNicknameResponseDto> updateNickname(
            @RequestParam Long userId,
            @Valid @RequestBody UpdateNicknameRequestDto request) {
        return ResponseEntity.ok(userService.updateNickname(userId, request));
    }

    // ── MY-04: 프로필 사진 업로드 ──
    @PostMapping("/me/profile-image")
    public ResponseEntity<UpdateProfileImageResponseDto> updateProfileImage(
            @RequestParam Long userId,
            @RequestParam("image") MultipartFile image) {

        String imageUrl = fileStorageService.store(image, "profiles");
        return ResponseEntity.ok(userService.updateProfileImage(userId, imageUrl));
    }

    // ── MY-05: 분철 구매 목록 ──
    @GetMapping("/me/purchases")
    public ResponseEntity<Page<PurchaseListResponseDto>> getPurchaseList(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "OPEN") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getPurchaseList(userId, status, pageable));
    }

    // ── MY-07: 분철 판매 목록 ──
    @GetMapping("/me/sales")
    public ResponseEntity<Page<SaleListResponseDto>> getSaleList(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "OPEN") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getSaleList(userId, status, pageable));
    }

    // ── MY-10: 내가 작성한 커뮤니티 글 ──
    @GetMapping("/me/community-posts")
    public ResponseEntity<Page<MyCommunityPostResponseDto>> getMyCommunityPosts(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getMyCommunityPosts(userId, pageable));
    }

    // ── MY-11: 받은 후기 전체 목록 ──
    @GetMapping("/me/reviews")
    public ResponseEntity<Page<MyPostReviewResponseDto>> getReceivedReviews(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getReceivedReviews(userId, pageable));
    }

    // ── MY-14: 신뢰 점수 상세 조회 ──
    @GetMapping("/{userId}/trust-score")
    public ResponseEntity<TrustScoreResponseDto> getTrustScore(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getTrustScore(userId));
    }
}
