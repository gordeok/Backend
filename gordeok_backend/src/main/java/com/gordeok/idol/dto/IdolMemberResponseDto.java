package com.gordeok.idol.dto;

import com.gordeok.idol.entity.IdolMember;
import lombok.Getter;

@Getter
public class IdolMemberResponseDto {

    private Long id;
    private Long idolId;
    private String name;

    public IdolMemberResponseDto(IdolMember member) {
        this.id = member.getId();
        this.idolId = member.getIdolId();
        this.name = member.getName();
    }
}
