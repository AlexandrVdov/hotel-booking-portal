package com.example.hotel_booking_portal.aop;

import com.example.hotel_booking_portal.exception.AlreadyExistsException;
import com.example.hotel_booking_portal.service.UserService;
import com.example.hotel_booking_portal.web.model.request.UpsertUserRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class UserCheckAspect {

    private final UserService userService;

    @Before("@annotation(checkUserExists)")
    public void checkUserExists(JoinPoint joinPoint, CheckUserExists checkUserExists) {

        Object[] args = joinPoint.getArgs();
        UpsertUserRequest userRequest = (UpsertUserRequest) args[0];

        if (userService.existsByUsername(userRequest.getUsername())) {
            throw new AlreadyExistsException("Пользователь с таким именем уже зарегистрирован");
        }

        if (userService.existsByEmail(userRequest.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким почтой уже зарегистрирован");
        }
    }
}
