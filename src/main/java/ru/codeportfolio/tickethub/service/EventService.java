package ru.codeportfolio.tickethub.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Service;
import ru.codeportfolio.tickethub.dto.EventRequestDto;
import ru.codeportfolio.tickethub.dto.EventResponseDto;
import ru.codeportfolio.tickethub.mapper.EventMapper;
import ru.codeportfolio.tickethub.model.Event;
import ru.codeportfolio.tickethub.repository.EventRepository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Cache<Long, Event> eventsCache = Caffeine.newBuilder()
            .expireAfterAccess(30,TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    public void createEvent(EventRequestDto eventRequestDto) {
        Event event = eventRepository.save(Event.builder()
                .name(eventRequestDto.name())
                .date(eventRequestDto.date())
                .totalSeats(eventRequestDto.totalSeats())
                .availableSeats(eventRequestDto.availableSeats())
                .build());
        log.info("Saved event {}, id: {}.", event.getName(), event.getId());
        putEventsToCache(eventRepository.findAll());
    }

    public List<EventResponseDto> getAllEvents() {
        List<Event> events;
        if (eventRepository.count() == eventsCache.estimatedSize()) {
            events = List.of((Event) eventsCache.asMap().values());
        } else {
            events = eventRepository.findAll();
        }

        List<EventResponseDto> eventResponseDtoList = eventMapper.toDtoList(events);
        eventResponseDtoList.sort(Comparator.comparing(EventResponseDto::date));
        return eventResponseDtoList;

    }

    private void putEventsToCache(List<Event> events) {
        eventsCache.putAll(
                events.stream().collect(Collectors.toMap(
                        Event::getId,
                        Function.identity()
                ))

        );
    }

}
