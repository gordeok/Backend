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
    private Long idolId;
    private Long albumId;
    private String title;
    private String description;
    private Integer totalPrice;
    private Integer shippingFee;
    private String status;
    private LocalDateTime createdAt;
    private List<MemberItemResponseDto> memberItems;

    public PostResponseDto(Post post, String nickname, List<MemberItemResponseDto> memberItems) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.nickname = nickname;
        this.idolId = post.getIdolId();
        this.albumId = post.getAlbumId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.totalPrice = post.getTotalPrice();
        this.shippingFee = post.getShippingFee();
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
        this.memberItems = memberItems;
    }
}
