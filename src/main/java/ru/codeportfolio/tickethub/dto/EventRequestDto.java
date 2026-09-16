package ru.codeportfolio.tickethub.dto;

import java.time.Instant;

public record EventRequestDto(
        String name,
        Instant date,
        Integer totalSeats,
        Integer availableSeats
) {
}
