package ru.codeportfolio.tickethub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codeportfolio.tickethub.dto.TransferRequestDto;
import ru.codeportfolio.tickethub.service.TransferService;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
@Slf4j
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transfer(
            @RequestBody TransferRequestDto transferRequestDto) {
        transferService.execute(transferRequestDto);
        return ResponseEntity.ok().build();
    }
}
