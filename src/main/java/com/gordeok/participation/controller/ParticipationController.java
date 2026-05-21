package com.gordeok.participation.controller;

import com.gordeok.participation.dto.CreateParticipationRequestDto;
import com.gordeok.participation.dto.CreateParticipationResponseDto;
import com.gordeok.participation.service.ParticipationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/members/{memberItemId}/participations")
public class ParticipationController {

    private final ParticipationService participationService;

    @PostMapping
    public CreateParticipationResponseDto createParticipation(
            @PathVariable Long postId,
            @PathVariable Long memberItemId,
            @RequestParam Long userId,
            @Valid @RequestBody CreateParticipationRequestDto request
    ) {

        return participationService.createParticipation(
                postId,
                memberItemId,
                userId,
                request
        );
    }
}