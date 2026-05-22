package com.gordeok.user.dto;

import com.gordeok.post.entity.Post;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class SaleListResponseDto {

    private Long postId;
    private String postTitle;
    private String thumbnailUrl;
    private String postStatus;       // OPEN / COMPLETED
    private Long participantCount;
    private LocalDateTime createdAt;

    public SaleListResponseDto(Post post, Long participantCount) {
        this.postId = post.getId();
        this.postTitle = post.getTitle();
        this.thumbnailUrl = post.getImageUrl();
        this.postStatus = post.getStatus();
        this.participantCount = participantCount;
        this.createdAt = post.getCreatedAt();
    }
}
