package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codeportfolio.tickethub.dto.StatsResponseDto;

import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final AtomicLong bookingCounter;
    private final AtomicLong transferCounter;

    public StatsResponseDto getStats() {
        return new StatsResponseDto(bookingCounter.get(), transferCounter.get());
    }
}
