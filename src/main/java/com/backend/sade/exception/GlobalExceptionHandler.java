package com.backend.sade.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //Tüm genel istisnaları yakalar ve loglar.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Yanlış türdeki metot argümanları için hata işleme.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type mismatch error: {}", ex.getMessage());
        return new ResponseEntity<>("Invalid parameter type. Please check your inputs.", HttpStatus.BAD_REQUEST);
    }

    //Veritabanı bütünlüğü ihlallerini ele alır.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return new ResponseEntity<>("Duplicate data detected. Please check your inputs.", HttpStatus.CONFLICT);
    }

    //Uygulama genelinde meydana gelen StackOverflowError hatalarını yakalar.
    @ExceptionHandler(StackOverflowError.class)
    public ResponseEntity<String> handleStackOverflowError(StackOverflowError ex) {
        log.error("Stack overflow error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("A system error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
