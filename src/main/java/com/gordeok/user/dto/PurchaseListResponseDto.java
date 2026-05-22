package com.gordeok.user.dto;

import com.gordeok.participation.entity.Participation;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PurchaseListResponseDto {

    private Long participationId;
    private Long postId;
    private String postTitle;
    private String memberName;     // 선택한 멤버 이름
    private String thumbnailUrl;   // Post 대표 이미지
    private String postStatus;     // OPEN / COMPLETED
    private Boolean canWriteReview;
    private LocalDateTime createdAt;

    public PurchaseListResponseDto(Participation participation, boolean canWriteReview) {
        this.participationId = participation.getId();
        this.postId = participation.getPost().getId();
        this.postTitle = participation.getPost().getTitle();
        this.memberName = participation.getMemberItem() != null
                ? participation.getMemberItem().getMemberName() : "";
        this.thumbnailUrl = participation.getPost().getImageUrl();
        this.postStatus = participation.getPost().getStatus();
        this.canWriteReview = canWriteReview;
        this.createdAt = participation.getCreatedAt();
    }
}
