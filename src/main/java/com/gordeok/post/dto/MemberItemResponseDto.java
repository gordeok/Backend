package com.gordeok.post.dto;

import com.gordeok.post.entity.MemberItem;
import lombok.Getter;

@Getter
public class MemberItemResponseDto {

    private Long id;
    private String memberName;
    private Integer price;
    private String status; // 한글 상태값: "모집중", "예약중", "모집완료"

    public MemberItemResponseDto(MemberItem memberItem) {
        this.id = memberItem.getId();
        this.memberName = memberItem.getMemberName();
        this.price = memberItem.getPrice();
        this.status = convertStatus(memberItem.getStatus());
    }

    // 영문 status → 한글 변환
    private String convertStatus(String status) {
        if (status == null) return "모집중";
        return switch (status) {
            case "AVAILABLE" -> "모집중";
            case "RESERVED"  -> "예약중";
            case "COMPLETED" -> "모집완료";
            default          -> status;
        };
    }
}
