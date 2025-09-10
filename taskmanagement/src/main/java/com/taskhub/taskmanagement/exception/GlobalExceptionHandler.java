package com.taskhub.taskmanagement.exception;

import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        if (model != null) {
            model.addAttribute("errors", errors);
            return new ModelAndView("error-page");
        } else {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    public Object handleNoSuchElementException(NoSuchElementException ex,Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", ex.getMessage());
            return new ModelAndView("error-page");
        }
        else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }}

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException ex,Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", ex.getMessage());
            return new ModelAndView("error-page");
        }
        else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(NullPointerException.class)
    public Object handleNullPointerException(NullPointerException ex,Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", ex.getMessage());
            return new ModelAndView("error-page");
        } else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex, Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", ex.getMessage());
            return new ModelAndView("error-page");
        } else {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
