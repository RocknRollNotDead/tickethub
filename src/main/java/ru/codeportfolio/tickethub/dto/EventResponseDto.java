package ru.codeportfolio.tickethub.dto;

import java.time.Instant;

public record EventResponseDto(
        Long id,
        String name,
        Instant date,
        Integer totalSeats,
        Integer availableSeats
) {
}
