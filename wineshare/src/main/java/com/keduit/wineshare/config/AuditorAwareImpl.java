package com.keduit.wineshare.config;//package com.keduit.wineshare.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware {
  @Override
  public Optional getCurrentAuditor() {

    // 로그인 유저 정보가 담겨있는 객체?
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    String memberId = "";
    if(authentication != null){ //authentication이 널 이아니면 = 로그인 되어있으면
      memberId = authentication.getName();  //사용자이름을 리턴하게끔
    }

    return Optional.of(memberId);

  }
}
