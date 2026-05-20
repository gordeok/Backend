package com.gordeok.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String selectionType;   // 선택 방식 (B2 신규 필드)
    private Boolean albumIncluded;  // 앨범 포함 여부 (B2 신규 필드)
    private String shippingFeeType; // 배송비 방식 (B2 신규 필드)

    @NotNull(message = "멤버 아이템 정보를 입력해주세요.")
    private List<MemberItemRequestDto> memberItems;
}
