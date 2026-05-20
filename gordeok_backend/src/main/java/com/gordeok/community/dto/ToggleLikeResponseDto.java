package com.gordeok.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToggleLikeResponseDto {

    private Boolean liked;
    private Integer likeCount;
    private String message;
}