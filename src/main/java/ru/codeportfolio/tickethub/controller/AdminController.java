package ru.codeportfolio.tickethub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codeportfolio.tickethub.service.BookingService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final BookingService bookingService;

    @PostMapping("/maintenance")
    public ResponseEntity<Void> turnOnMaintenanceMode() {
        bookingService.turnOnMaintenanceMode();
        return ResponseEntity.ok().build();
    }

}
