package ru.codeportfolio.tickethub.mapper;

import org.mapstruct.Mapper;
import ru.codeportfolio.tickethub.dto.UserResponseDto;
import ru.codeportfolio.tickethub.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User user);
}
