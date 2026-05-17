package com.gordeok.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreatePostRequestDto {

    private String title;

    private String description;

    private String imageUrl;

    private String idolName;

    private String albumName;

    private String selectionType;

    private Boolean albumIncluded;

    private String shippingFeeType;

    private List<MemberItemRequestDto> memberItems;
}