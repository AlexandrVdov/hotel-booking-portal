package com.example.hotel_booking_portal.web.controller;

import com.example.hotel_booking_portal.exception.AlreadyExistsException;
import com.example.hotel_booking_portal.exception.EntityNotAvailableException;
import com.example.hotel_booking_portal.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.web.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException ex) {
        log.error("Ошибка при попытке получить сущность", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(EntityNotAvailableException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotAvailableException ex) {
        log.error("Сущность не доступна", ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> alreadyExists(AlreadyExistsException ex) {
        log.error("Сущность уже существует", ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> notValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        String errorMessage = String.join("; ", errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> notAllow(AuthorizationDeniedException ex) {
        log.error("Нет прав на это действие", ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Нет прав на это действие"));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> validRating(HandlerMethodValidationException ex) {
        log.error("Не правильно указан рейтинг", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Рейтинг должен быть в диапазоне от 1 до 5"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> allExceptions(Exception ex) {
        log.error("Необработанная ошибка: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getLocalizedMessage()));
    }
}
