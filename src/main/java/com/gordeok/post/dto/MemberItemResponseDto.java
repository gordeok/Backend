package com.gordeok.post.dto;

import com.gordeok.post.entity.MemberItem;
import lombok.Getter;

@Getter
public class MemberItemResponseDto {

    private Long id;
    private Long memberId;
    private Integer price;
    private String status;

    public MemberItemResponseDto(MemberItem memberItem) {
        this.id = memberItem.getId();
        this.memberId = memberItem.getMemberId();
        this.price = memberItem.getPrice();
        this.status = memberItem.getStatus();
    }
}
