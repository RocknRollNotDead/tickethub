package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codeportfolio.tickethub.dto.TransferRequestDto;
import ru.codeportfolio.tickethub.model.User;
import ru.codeportfolio.tickethub.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransferService {
    private final UserRepository userRepository;

    public void execute(TransferRequestDto transferRequestDto) {
        User user = userRepository.findById(transferRequestDto.userId()).orElseThrow();
        User targetUser = userRepository.findById(transferRequestDto.targetUserId()).orElseThrow();

        synchronized (user.getId()){
            synchronized (targetUser.getId()){
                user.reduceBalance(transferRequestDto.transferSum());
                targetUser.addBalance(transferRequestDto.transferSum());
            }
        }

    }
}
