package com.gordeok.community.dto;

import com.gordeok.community.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityCommentResponseDto {

    private Long commentId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String content;
    private LocalDateTime createdAt;

    public CommunityCommentResponseDto(Comment comment) {
        this.commentId = comment.getId();

        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.profileImage = comment.getUser().getProfileImage();

        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}