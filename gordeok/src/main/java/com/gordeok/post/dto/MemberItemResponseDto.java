package com.gordeok.post.dto;

import com.gordeok.post.entity.MemberItem;
import lombok.Getter;

@Getter
public class MemberItemResponseDto {

    private Long memberItemId;
    private String memberName;
    private Integer price;
    private String status;

    public MemberItemResponseDto(MemberItem memberItem) {

        this.memberItemId = memberItem.getId();
        this.memberName = memberItem.getMemberName();
        this.price = memberItem.getPrice();
        this.status = memberItem.getStatus();
    }
}