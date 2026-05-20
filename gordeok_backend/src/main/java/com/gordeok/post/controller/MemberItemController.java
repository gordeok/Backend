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
     * 멤버 선택 + 참여글 저장 + 채팅방 입장
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
