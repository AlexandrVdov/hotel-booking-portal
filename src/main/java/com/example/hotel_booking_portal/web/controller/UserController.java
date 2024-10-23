package com.example.hotel_booking_portal.web.controller;

import com.example.hotel_booking_portal.aop.CheckUserExists;
import com.example.hotel_booking_portal.entity.RoleType;
import com.example.hotel_booking_portal.entity.UserRole;
import com.example.hotel_booking_portal.service.UserService;
import com.example.hotel_booking_portal.web.model.request.UpsertUserRequest;
import com.example.hotel_booking_portal.web.model.request.UserFilter;
import com.example.hotel_booking_portal.web.model.response.UpdateUserResponse;
import com.example.hotel_booking_portal.web.model.response.UserListResponse;
import com.example.hotel_booking_portal.web.model.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserListResponse> findAll(@Valid UserFilter filter) {
        return ResponseEntity.ok(userService.findAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @CheckUserExists
    public ResponseEntity<UpdateUserResponse> create(@RequestBody @Valid UpsertUserRequest request,
                                                     @RequestParam @Valid RoleType roleType) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(request, UserRole.from(roleType)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid UpsertUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
