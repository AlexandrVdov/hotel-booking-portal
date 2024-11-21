package com.example.aop;

import com.example.exception.AlreadyExistsException;
import com.example.service.UserService;
import com.example.web.request.UpsertUserRequest;
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
