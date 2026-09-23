package ru.codeportfolio.tickethub.dto;

public record StatsResponseDto(
        Long bookingCounter,
        Long transferCounter
) {
}
