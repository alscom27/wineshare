package com.keduit.wineshare.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body("유효하지 않은 게시판 상태입니다. : " + e.getMessage());
  }

}
