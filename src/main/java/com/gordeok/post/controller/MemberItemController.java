package com.gordeok.post.controller;

import com.gordeok.post.dto.MemberSelectRequestDto;
import com.gordeok.post.dto.MemberSelectResponseDto;
import com.gordeok.post.service.MemberItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member-items")
@RequiredArgsConstructor
public class MemberItemController {

    private final MemberItemService memberItemService;

    /**
     * [1단계] 멤버 슬롯 클릭 → RESERVED (슬롯 잠금만)
     * POST /api/member-items/{memberItemId}/reserve?buyerId=X
     */
    @PostMapping("/{memberItemId}/reserve")
    public ResponseEntity<Void> reserveSlot(
            @PathVariable Long memberItemId,
            @RequestParam Long buyerId) {
        memberItemService.reserveSlot(memberItemId, buyerId);
        return ResponseEntity.ok().build();
    }

    /**
     * [2단계] 참여글 작성 완료 + 채팅방 입장 버튼 → COMPLETED + 참여글 생성 + 채팅방 생성/입장
     * POST /api/member-items/{memberItemId}/select
     * Body: { buyerId, recipientName, phoneNumber, convenienceStore, request }
     * Response: { chatRoomId, message }
     */
    @PostMapping("/{memberItemId}/select")
    public ResponseEntity<MemberSelectResponseDto> selectMember(
            @PathVariable Long memberItemId,
            @RequestBody MemberSelectRequestDto dto) {
        MemberSelectResponseDto response = memberItemService.selectMember(memberItemId, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * 멤버 선택 취소 + 참여글 삭제
     * PATCH /api/member-items/{memberItemId}/cancel?buyerId=1
     */
    @PatchMapping("/{memberItemId}/cancel")
    public ResponseEntity<Void> cancelMember(
            @PathVariable Long memberItemId,
            @RequestParam Long buyerId) {
        memberItemService.cancelMember(memberItemId, buyerId);
        return ResponseEntity.ok().build();
    }
}
