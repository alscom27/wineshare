package com.keduit.wineshare.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/about")
@Log4j2
public class TestController {

  @GetMapping("/header")
  public void list(){
    log.info("header");
  }


  // 사이트 소개 임시 컨트롤러
  @GetMapping("/aboutUs")
  public String dList(){
    log.info("aboutUs");
    return "about/aboutUs";
  }

}

