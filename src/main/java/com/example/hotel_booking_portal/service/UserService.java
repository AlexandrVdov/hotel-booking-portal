package com.example.hotel_booking_portal.service;

import com.example.hotel_booking_portal.entity.User;
import com.example.hotel_booking_portal.entity.UserRole;
import com.example.hotel_booking_portal.web.model.request.UpsertUserRequest;
import com.example.hotel_booking_portal.web.model.request.UserFilter;
import com.example.hotel_booking_portal.web.model.response.UpdateUserResponse;
import com.example.hotel_booking_portal.web.model.response.UserListResponse;
import com.example.hotel_booking_portal.web.model.response.UserResponse;

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
