package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codeportfolio.tickethub.dto.TransferRequestDto;
import ru.codeportfolio.tickethub.model.User;
import ru.codeportfolio.tickethub.repository.UserRepository;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransferService {
    private final UserRepository userRepository;
    private final ConcurrentHashMap<Long, Object> locks = new ConcurrentHashMap<>();

    public void execute(TransferRequestDto transferRequestDto) {
        User user = userRepository.findById(transferRequestDto.userId()).orElseThrow();
        User targetUser = userRepository.findById(transferRequestDto.targetUserId()).orElseThrow();

        Object lockA = locks.computeIfAbsent(
                        user.getId() < targetUser.getId() ?
                        user.getId() : targetUser.getId(),
                        id -> new Object());
        Object lockB = locks.computeIfAbsent(
                user.getId() < targetUser.getId() ?
                targetUser.getId() : user.getId(),
                id -> new Object());

        synchronized (lockA) {
            synchronized (lockB) {
                user.reduceBalance(transferRequestDto.transferSum());
                targetUser.addBalance(transferRequestDto.transferSum());
                userRepository.save(user);
                userRepository.save(targetUser);
            }
        }
    }
}