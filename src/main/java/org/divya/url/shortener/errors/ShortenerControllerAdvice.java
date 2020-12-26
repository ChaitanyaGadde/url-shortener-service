package org.divya.url.shortener.errors;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ShortenerControllerAdvice {

  @ExceptionHandler(value = ShorteningExceptions.class)
  public ResponseEntity<?> handleException(ShorteningExceptions shorteningExceptions) {
    return new ResponseEntity<>(new ErrorResponse(shorteningExceptions.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Value
  public static class ErrorResponse {

    String error;
  }
}
