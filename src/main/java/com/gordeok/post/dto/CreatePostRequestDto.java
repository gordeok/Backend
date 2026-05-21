package com.gordeok.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePostRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String description;
    private String imageUrl;

    @NotBlank(message = "아이돌명을 입력해주세요.")
    private String idolName;

    private String albumName;

    private List<String> components;    // 구성품 설정
    private String shippingFeeType;     // 배송 방법

    @NotNull(message = "멤버 아이템 정보를 입력해주세요.")
    private List<MemberItemRequestDto> memberItems;
}
