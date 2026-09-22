package ru.codeportfolio.tickethub.dto;

public record TransferRequestDto(
       Long userId,
       Long targetUserId,
       Long transferSum

) {
}
