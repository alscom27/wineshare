package com.keduit.wineshare.exception;

import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> NotFoundExceptionHandler(NotFoundException e) {
    return ResponseEntity.badRequest().body("찾을 수 없는 페이지입니다. : " + e.getMessage());
  }

}
