package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codeportfolio.tickethub.dto.EventRequestDto;
import ru.codeportfolio.tickethub.dto.EventResponseDto;
import ru.codeportfolio.tickethub.mapper.EventMapper;
import ru.codeportfolio.tickethub.model.Event;
import ru.codeportfolio.tickethub.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public void createEvent(EventRequestDto eventRequestDto) {
        eventRepository.save(Event.builder()
                        .name(eventRequestDto.name())
                        .date(eventRequestDto.date())
                        .totalSeats(eventRequestDto.totalSeats())
                        .availableSeats(eventRequestDto.availableSeats())
                        .build());
    }

    public List<EventResponseDto> getAllEvents(){
        List<Event> events = eventRepository.findAll();
        return eventMapper.toDtoList(events);

    }
}
