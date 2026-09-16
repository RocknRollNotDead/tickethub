package ru.codeportfolio.tickethub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.codeportfolio.tickethub.dto.EventRequestDto;
import ru.codeportfolio.tickethub.dto.EventResponseDto;
import ru.codeportfolio.tickethub.service.BookingService;
import ru.codeportfolio.tickethub.service.EventService;

import java.util.List;

@RestController("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final BookingService bookingService;

    @PostMapping
    public void create(EventRequestDto eventRequestDto) {
        eventService.createEvent(eventRequestDto);
    }

    @GetMapping
    public List<EventResponseDto> getEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("/{id}/book")
    public void createBooking(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        bookingService.createBooking(id, userId);
    }

}
