package ru.codeportfolio.tickethub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codeportfolio.tickethub.dto.StatsResponseDto;
import ru.codeportfolio.tickethub.service.StatsService;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<StatsResponseDto> transfer() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
