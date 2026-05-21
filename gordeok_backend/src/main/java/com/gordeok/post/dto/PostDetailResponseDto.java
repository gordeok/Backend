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
    private List<String> components;
    private String shippingFeeType;
    private String status;
    private Integer scrapCount;
    private SellerInfoDto seller;
    private List<MemberItemResponseDto> memberItems;
    private boolean bookmarked;
}