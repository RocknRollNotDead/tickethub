package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.codeportfolio.tickethub.dto.EventRequestDto;
import ru.codeportfolio.tickethub.dto.EventResponseDto;
import ru.codeportfolio.tickethub.mapper.EventMapper;
import ru.codeportfolio.tickethub.model.Event;
import ru.codeportfolio.tickethub.repository.EventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public void createEvent(EventRequestDto eventRequestDto) {
        Event event = eventRepository.save(Event.builder()
                        .name(eventRequestDto.name())
                        .date(eventRequestDto.date())
                        .totalSeats(eventRequestDto.totalSeats())
                        .availableSeats(eventRequestDto.availableSeats())
                        .build());
        log.info("Saved event {}, id: {}.", event.getName(), event.getId());
    }

    public List<EventResponseDto> getAllEvents(){
        List<Event> events = eventRepository.findAll();
        List<EventResponseDto> eventResponseDtoList = eventMapper.toDtoList(events);
        log.info("return events: {} id {} {} id {}",
                eventResponseDtoList.get(3).name(),
                eventResponseDtoList.get(3).id(),
                eventResponseDtoList.get(4).name(),
                eventResponseDtoList.get(4).id());
        return eventResponseDtoList;

    }
}
