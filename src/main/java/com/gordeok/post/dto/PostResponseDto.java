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
    private String profileImage;       // 판매자 프로필 사진
    private Long idolId;
    private Long albumId;
    private String title;
    private String description;
    private Integer totalPrice;
    private Integer shippingFee;
    private String status;
    private LocalDateTime createdAt;
    private List<MemberItemResponseDto> memberItems;
    private boolean isAlmostFull;      // 마감임박 여부

    public PostResponseDto(Post post, String nickname, String profileImage,
                           List<MemberItemResponseDto> memberItems) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.idolId = post.getIdolId();
        this.albumId = post.getAlbumId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.totalPrice = post.getTotalPrice();
        this.shippingFee = post.getShippingFee();
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
        this.memberItems = memberItems;

        // 마감임박: 전체 멤버 중 모집완료 비율이 2/3 이상이면 true
        if (!memberItems.isEmpty()) {
            long completedCount = memberItems.stream()
                    .filter(item -> "COMPLETED".equals(item.getStatus()))
                    .count();
            this.isAlmostFull = completedCount >= memberItems.size() * 2.0 / 3.0;
        } else {
            this.isAlmostFull = false;
        }
    }
}
