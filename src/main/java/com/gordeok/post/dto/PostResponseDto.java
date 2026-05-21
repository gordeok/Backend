package com.gordeok.post.dto;

import com.gordeok.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {

    private Long id;
    private Long userId;
    private String nickname;
    private String profileImage;
    private String idolName;
    private String albumName;
    private String title;
    private String description;
    private String imageUrl;
    private List<String> components;
    private String shippingFeeType;
    private String status;             // 한글 상태값: "모집중", "모집완료"
    private Integer viewCount;
    private Integer scrapCount;
    private LocalDateTime createdAt;
    private List<MemberItemResponseDto> memberItems;
    private boolean isAlmostFull;      // 마감임박 여부

    public PostResponseDto(Post post, List<MemberItemResponseDto> memberItems) {
        this.id = post.getId();
        this.userId = post.getUser() != null ? post.getUser().getId() : null;
        this.nickname = post.getUser() != null ? post.getUser().getNickname() : "알 수 없음";
        this.profileImage = post.getUser() != null ? post.getUser().getProfileImage() : null;
        this.idolName = post.getIdolName();
        this.albumName = post.getAlbumName();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.imageUrl = post.getImageUrl();
        this.components = post.getComponents();
        this.shippingFeeType = post.getShippingFeeType();
        this.status = convertStatus(post.getStatus());
        this.viewCount = post.getViewCount();
        this.scrapCount = post.getScrapCount();
        this.createdAt = post.getCreatedAt();
        this.memberItems = memberItems;

        // 마감임박: 전체 멤버 중 모집완료 비율이 2/3 이상이면 true
        if (!memberItems.isEmpty()) {
            long completedCount = memberItems.stream()
                    .filter(item -> "모집완료".equals(item.getStatus()))
                    .count();
            this.isAlmostFull = completedCount >= memberItems.size() * 2.0 / 3.0;
        } else {
            this.isAlmostFull = false;
        }
    }

    private String convertStatus(String status) {
        if (status == null) return "모집중";
        return switch (status) {
            case "OPEN"   -> "모집중";
            case "CLOSED" -> "모집완료";
            default       -> status;
        };
    }
}
