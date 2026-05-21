package com.gordeok.post.dto;

import com.gordeok.idol.dto.IdolMemberResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageAnalyzeResponseDto {

    private String idolName;
    private String albumName;
    private List<IdolMemberResponseDto> members;
    private boolean isConfident;
}
