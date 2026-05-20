package com.gordeok.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class MemberItemRequestDto {

    @NotBlank(message = "멤버명은 필수입니다.") // 수정: 멤버명 필수 검증 추가
    private String memberName;

    @NotNull(message = "가격은 필수입니다.") // 수정: 가격 필수 검증 추가
    @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.") // 수정: 음수 가격 방지
    private Integer price;
}