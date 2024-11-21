package com.example.service;

import com.example.domain.User;
import com.example.domain.UserRole;
import com.example.web.request.UpsertUserRequest;
import com.example.web.request.UserFilter;
import com.example.web.response.UpdateUserResponse;
import com.example.web.response.UserListResponse;
import com.example.web.response.UserResponse;

public interface UserService {

    UserListResponse findAll(UserFilter filter);

    UserResponse findById(Long userId);

    UpdateUserResponse save(UpsertUserRequest userRequest, UserRole role);

    UpdateUserResponse update(Long userId, UpsertUserRequest userRequest);

    void deleteById(Long userId);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
