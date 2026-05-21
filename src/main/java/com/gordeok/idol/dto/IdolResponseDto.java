package com.gordeok.idol.dto;

import com.gordeok.idol.entity.Idol;
import lombok.Getter;

@Getter
public class IdolResponseDto {

    private Long id;
    private String name;
    private String code; // 프론트 string ID (예: "boynextdoor")

    public IdolResponseDto(Idol idol) {
        this.id = idol.getId();
        this.name = idol.getName();
        this.code = idol.getCode();
    }
}
