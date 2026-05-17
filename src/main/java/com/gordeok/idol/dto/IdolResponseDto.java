package com.gordeok.idol.dto;

import com.gordeok.idol.entity.Idol;
import lombok.Getter;

@Getter
public class IdolResponseDto {

    private Long id;
    private String name;

    public IdolResponseDto(Idol idol) {
        this.id = idol.getId();
        this.name = idol.getName();
    }
}
