package com.keduit.wineshare.config;//package com.keduit.wineshare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;

@Configuration
@EnableAspectJAutoProxy
public class AuditConfig {

  @Bean
  public AuditorAware<String> auditorProvider(){
    // 등록자, 수정자를 처리해주는 AuditorAwareImpl을 Bean으로 등록
    return new AuditorAwareImpl();
  }
}