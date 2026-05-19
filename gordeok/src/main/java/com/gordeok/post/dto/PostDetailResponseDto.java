package com.gordeok.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostDetailResponseDto {

    private Long postId;
    private String title;
    private String description;
    private String imageUrl;
    private String idolName;
    private String albumName;
    private String components;
    private String deliveryMethods;
    private String status;
    // private Integer viewCount; -> 조회수 필요한가
    private Integer scrapCount;
    private SellerInfoDto seller;
    private List<MemberItemResponseDto> memberItems;

    private boolean bookmarked; // 수정: 현재 사용자의 북마크 여부
}