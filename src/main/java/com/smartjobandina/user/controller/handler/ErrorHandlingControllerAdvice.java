package com.smartjobandina.user.controller.handler;

import com.smartjobandina.user.domain.dto.ResponseDto;
import com.smartjobandina.user.exception.EmailAlrealdyRegisteredException;
import com.smartjobandina.user.exception.EmailNotMatchException;
import com.smartjobandina.user.exception.PasswordNotMatchException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="christianram19@hotmail.com">Christian Ramirez</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    public static final String MESSAGE_CONSTRAIN_VIOLATION_ERROR = "Request data has constrains violations";
    public static final String MESSAGE_ARGUMENT_NOT_VALID_ERROR = "Request data has not valid arguments";

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(errors, MESSAGE_CONSTRAIN_VIOLATION_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(errors, MESSAGE_ARGUMENT_NOT_VALID_ERROR));
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlePasswordNotMatchException(PasswordNotMatchException ex) {

        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(ex.getMessage()));
    }

    @ExceptionHandler(EmailNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlePasswordNotMatchException(EmailNotMatchException ex) {

        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlrealdyRegisteredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleEmailAlrealdyExistException(EmailAlrealdyRegisteredException ex) {

        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleException(DataAccessException ex) {

        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto<>("Data access exception"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleException(Exception ex) {

        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto<>(ex.getMessage()));
    }
}