package com.example.service.impl;

import com.example.domain.User;
import com.example.domain.UserRole;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.UserMapper;

import com.example.repository.UserRepository;
import com.example.service.KafkaAuthMessageService;
import com.example.service.UserService;
import com.example.utils.BeanUtils;
import com.example.web.request.UpsertUserRequest;
import com.example.web.request.UserFilter;
import com.example.web.response.UpdateUserResponse;
import com.example.web.response.UserListResponse;
import com.example.web.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final KafkaAuthMessageService messageService;

    @Override
    public UserListResponse findAll(UserFilter filter) {
        return userMapper.userListToUserListResponse(userRepository.findAll(
                        PageRequest.of(filter.getPageNumber(), filter.getPageSize()))
                .getContent()
        );
    }

    @Override
    public UserResponse findById(Long userId) {
        return userMapper.userToResponse(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Пользователь не найден с Id {0}", userId)))
        );
    }

    @Override
    public UpdateUserResponse save(UpsertUserRequest userRequest, UserRole role) {
        User newUser = userMapper.requestToUser(userRequest);

        newUser.setRole(role);
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepository.saveAndFlush(newUser);
        messageService.sendRegistrationMessage(newUser);

        return userMapper.userToUpdateResponse(newUser);
    }

    @Override
    public UpdateUserResponse update(Long userId, UpsertUserRequest userRequest) {
        User updatedUser = userMapper.requestToUser(userId, userRequest);

        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Пользователь не найден с Id {0}", userId)));

        BeanUtils.copyNonNullProperties(updatedUser, existedUser);
        existedUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        return userMapper.userToUpdateResponse(userRepository.saveAndFlush(existedUser));
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(MessageFormat.format(
                        "Пользователь не найден с именем {0}", username)));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
