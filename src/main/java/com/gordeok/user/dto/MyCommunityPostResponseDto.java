package com.gordeok.user.dto;

import com.gordeok.community.entity.CommunityPost;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MyCommunityPostResponseDto {

    private Long postId;
    private String category;
    private String title;
    private String preview;   // content 앞 80자
    private LocalDateTime createdAt;

    public MyCommunityPostResponseDto(CommunityPost post) {
        this.postId = post.getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.preview = post.getContent() != null && post.getContent().length() > 80
                ? post.getContent().substring(0, 80) + "..."
                : post.getContent();
        this.createdAt = post.getCreatedAt();
    }
}
