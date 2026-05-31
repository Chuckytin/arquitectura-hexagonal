package com.springboot.web.common.infrastructure.exceptions;

import com.springboot.web.category.domain.exception.CategoryNotFoundException;
import com.springboot.web.product.domain.exception.ProductNotFoundException;
import com.springboot.web.productdetail.domain.exception.ProductDetailNotFoundException;
import com.springboot.web.review.domain.exception.ReviewNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> badRequest(HttpServletRequest request, MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(
                        exception.getMessage(),
                        exception.getClass().getName(),
                        request.getRequestURI(),
                        errors
                ));
    }

    @ExceptionHandler({
            ProductNotFoundException.class,
            CategoryNotFoundException.class,
            ReviewNotFoundException.class,
            ProductDetailNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> notFound(HttpServletRequest request, RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(
                        exception.getMessage(),
                        exception.getClass().getName(),
                        request.getRequestURI()
                ));
    }
}