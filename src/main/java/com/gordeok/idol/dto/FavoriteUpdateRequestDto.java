package com.gordeok.idol.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FavoriteUpdateRequestDto {

    // 최애 그룹 저장 시: idolIds 사용
    // 최애 멤버 저장 시: memberIds 사용
    private List<Long> idolIds;
    private List<Long> memberIds;
}
