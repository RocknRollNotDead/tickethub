package ru.codeportfolio.tickethub.mapper;

import org.mapstruct.Mapper;
import ru.codeportfolio.tickethub.dto.EventResponseDto;
import ru.codeportfolio.tickethub.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventResponseDto toDto(Event event);
    List<EventResponseDto> toDtoList(List<Event> events);
}
