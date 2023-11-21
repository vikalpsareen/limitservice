package com.velocity.limits.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling exceptions across the application.
 * This class is annotated with {@code @ControllerAdvice}, indicating that
 * it provides centralized exception handling for all controllers.
 *
 * The {@code handleException} method is annotated with {@code @ExceptionHandler},
 * specifying that it handles exceptions of type {@code Exception}. It logs the
 * exception using SLF4J and returns a customized response indicating that
 * an exception occurred along with an HTTP 500 Internal Server Error status.
 *
 * To use this global exception handler, make sure to register it in your Spring
 * application context.
 *
 * Author: VSareen
 * Version: 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * SLF4J Logger for logging exception details.
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles exceptions of type {@code Exception} and logs the exception details.
     *
     * @param e The exception to be handled.
     * @return A ResponseEntity with a customized error message and HTTP 500 status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception
        logger.error("Exception occurred:", e);

        // You can customize the response or return a more detailed error message
        return new ResponseEntity<>("Exception occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
