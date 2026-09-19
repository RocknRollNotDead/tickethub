package ru.codeportfolio.tickethub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codeportfolio.tickethub.dto.EventRequestDto;
import ru.codeportfolio.tickethub.dto.EventResponseDto;
import ru.codeportfolio.tickethub.service.BookingService;
import ru.codeportfolio.tickethub.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody EventRequestDto eventRequestDto) {
        eventService.createEvent(eventRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping("/{id}/book")
    public void createBooking(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        bookingService.createBooking(id, userId);
    }

}
