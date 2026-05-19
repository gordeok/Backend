package com.gordeok.post.dto;

import jakarta.validation.Valid; // 수정: memberItems 내부 DTO 검증을 위한 import 추가
import jakarta.validation.constraints.NotBlank; // 수정: 빈 문자열 검증 import 추가
import jakarta.validation.constraints.NotEmpty; // 수정: 리스트 비어있는지 검증 import 추가
import lombok.Getter;
import java.util.List;

@Getter
public class CreatePostRequestDto {

    @NotBlank(message = "게시글 제목은 필수입니다.") // 수정: 제목 필수 검증 추가
    private String title;
    private String description;
    private String imageUrl;

    @NotBlank(message = "아이돌명은 필수입니다.") // 수정: 아이돌명 필수 검증 추가
    private String idolName;

    @NotBlank(message = "앨범명은 필수입니다.") // 수정: 앨범명 필수 검증 추가
    private String albumName;
    private String components;

    @NotBlank(message = "배송 방법은 필수입니다.") // 수정: 배송 방법 필수 검증 추가
    private String deliveryMethods;

    @Valid // 수정: 리스트 안의 MemberItemRequestDto까지 검증
    @NotEmpty(message = "멤버별 가격 목록은 필수입니다.") // 수정: 멤버 목록 비어있으면 등록 불가
    private List<MemberItemRequestDto> memberItems;
}