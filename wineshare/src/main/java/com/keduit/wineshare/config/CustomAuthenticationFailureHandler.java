package com.keduit.wineshare.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    String errorMessage;

    if (exception.getMessage().equals("사용자를 찾을 수 없습니다.")) {
      errorMessage = "사용자를 찾을 수 없습니다.";
    } else if(exception.getMessage().equals("탈퇴한 회원입니다.")) {
      errorMessage = "사용자를 찾을 수 없습니다.";
    }else{
      errorMessage = "아이디 또는 비밀번호가 틀렸습니다.";
    }


    // 로그인 페이지로 리다이렉트하고 오류 메시지 전달
    request.getSession().setAttribute("loginErrorMsg", errorMessage);
    response.sendRedirect("/members/login"); // 로그인 페이지로 리다이렉트

  }
}
