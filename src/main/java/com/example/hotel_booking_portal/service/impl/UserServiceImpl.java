package com.example.hotel_booking_portal.service.impl;

import com.example.hotel_booking_portal.entity.User;
import com.example.hotel_booking_portal.entity.UserRole;
import com.example.hotel_booking_portal.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.mapper.UserMapper;
import com.example.hotel_booking_portal.repository.UserRepository;
import com.example.hotel_booking_portal.service.UserService;
import com.example.hotel_booking_portal.utils.BeanUtils;
import com.example.hotel_booking_portal.web.model.request.UpsertUserRequest;
import com.example.hotel_booking_portal.web.model.request.UserFilter;
import com.example.hotel_booking_portal.web.model.response.UpdateUserResponse;
import com.example.hotel_booking_portal.web.model.response.UserListResponse;
import com.example.hotel_booking_portal.web.model.response.UserResponse;
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

        return userMapper.userToUpdateResponse(userRepository.saveAndFlush(newUser));
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
