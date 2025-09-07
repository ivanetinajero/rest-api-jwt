package dev.itinajero.app.controller;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Error de validación en la petición: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String message = error.getDefaultMessage();
            logger.info("Campo inválido: {} - {}", error.getField(), message);
            errors.put(error.getField(), message);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.warn("ConstraintViolationException: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            logger.info("Violación de restricción: {} - {}", field, violation.getMessage());
            errors.put(field, violation.getMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("Cuerpo de la petición mal formado o datos inválidos: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Cuerpo de la petición mal formado o datos inválidos");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Recurso no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("Error interno del servidor: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        error.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
