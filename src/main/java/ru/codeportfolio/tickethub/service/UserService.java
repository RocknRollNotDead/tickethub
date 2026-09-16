package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codeportfolio.tickethub.dto.UserResponseDto;
import ru.codeportfolio.tickethub.mapper.UserMapper;
import ru.codeportfolio.tickethub.model.User;
import ru.codeportfolio.tickethub.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto create(String name) {
        User user = User.builder()
                .name(name)
                .build();
        User createdUser = userRepository.save(user);
        return userMapper.toDto(createdUser);
    }

    @Transactional
    public void addMoney(Long rubles, Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found user!"));
        user.addBalance(rubles);
        userRepository.save(user);
    }
}
