package com.gordeok.community.dto;

import com.gordeok.community.entity.CommunityPost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityPostListResponseDto {

    private Long postId;
    private String category;
    private String title;
    private String contentPreview;
    private String imageUrl;
    private Long authorId;
    private String authorNickname;
    private String authorProfileImage;
    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;
    private LocalDateTime createdAt;

    public CommunityPostListResponseDto(CommunityPost post) {
        this.postId = post.getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.contentPreview = makePreview(post.getContent());
        this.imageUrl = getFirstImageUrl(post.getImageUrls());

        this.authorId = post.getUser().getId();
        this.authorNickname = post.getUser().getNickname();
        this.authorProfileImage = post.getUser().getProfileImage();

        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
    }

    private String makePreview(String content) {
        if (content == null) {
            return "";
        }

        if (content.length() <= 50) {
            return content;
        }

        return content.substring(0, 50) + "...";
    }

    private String getFirstImageUrl(String imageUrls) {
        if (imageUrls == null || imageUrls.isBlank()) {
            return null;
        }

        return imageUrls.split(",")[0];
    }
}