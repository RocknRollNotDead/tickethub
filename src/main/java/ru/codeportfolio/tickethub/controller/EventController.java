package ru.codeportfolio.tickethub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codeportfolio.tickethub.dto.EventRequestDto;
import ru.codeportfolio.tickethub.dto.EventResponseDto;
import ru.codeportfolio.tickethub.service.EventService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public void create(EventRequestDto eventRequestDto){
        eventService.createEvent(eventRequestDto);
    }

    @GetMapping
    public List<EventResponseDto> getEvents(){
        return eventService.getAllEvents();
    }

}
