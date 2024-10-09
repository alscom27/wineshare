package com.keduit.wineshare.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${uploadPath}")
  // 프로퍼티스에서 설정한 경로
  String uploadPath;

  // url 이 /images/**로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽도록 설정
  // .addResourceLocations(uploadPath) : 로컬 컴퓨터에서 읽어올 루트 경로를 설정
  // uploadPath : 실제 파일의 경로를 말한다.
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/images/**") // 접근 경로
        .addResourceLocations(uploadPath);    // 실제 파일 경로
  }
}
