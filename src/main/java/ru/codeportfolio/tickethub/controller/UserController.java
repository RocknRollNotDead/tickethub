package ru.codeportfolio.tickethub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.codeportfolio.tickethub.dto.UserResponseDto;
import ru.codeportfolio.tickethub.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(String name){
        UserResponseDto userResponseDto = userService.create(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @PostMapping("/balance")
    public void addMoney(Long rubles,
                        @RequestHeader String idFromHeader){
        Long id = Long.valueOf(idFromHeader);
        userService.addMoney(rubles, id);
    }
}
