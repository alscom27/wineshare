package com.keduit.wineshare.controller;

import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.service.WineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

  private final WineService wineService;

  public MainController(WineService wineService) {
    this.wineService = wineService;
  }

  @GetMapping("/")
  public String main(Model model) {

    // // 추천와인
    // 별점순 3개
    List<Wine> recommendedRatingWines = wineService.getRecommendedRatingWines();

    // 인기순(찜) 3개
    List<Wine> recommendedCellarWines = wineService.getRecommendedCellarWines();

    // 최신순 3개
    List<Wine> recommendedDescWines = wineService.getRecommendedDescWines();


    // 프로모션 3개를 엮어서.... 리스트로...
    // 와인바 1개
    // 보틀샵 1개
    // 비스트로 1개

    model.addAttribute("recommendedRatingWines",recommendedRatingWines);
    model.addAttribute("recommendedCellarWines",recommendedCellarWines);
    model.addAttribute("recommendedDescWines",recommendedDescWines);


    return "main";
  }
}
