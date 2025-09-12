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
    @ExceptionHandler(TaskNotFoundException.class)
    public Object handleTaskNotFoundException(TaskNotFoundException ex, Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", ex.getMessage());
            return new ModelAndView("error-page");
        } else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(TaskAlreadyExistsException.class)
    public Object handleTaskAlreadyExistsException(TaskAlreadyExistsException ex, Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", ex.getMessage());
            return new ModelAndView("error-page");
        } else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(InvalidTaskException.class)
    public Object handleInvalidTaskException(InvalidTaskException ex, Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", ex.getMessage());
            return new ModelAndView("error-page");
        } else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @ExceptionHandler(TaskValidationException.class)
    public Object handleTaskValidationException(TaskValidationException ex, Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", ex.getMessage());
            return new ModelAndView("error-page");
        } else {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

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
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex, Model model) {
        if (model != null) {
            model.addAttribute("errorMessage", "An unexpected error occurred");
            return new ModelAndView("error-page");
        } else {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
