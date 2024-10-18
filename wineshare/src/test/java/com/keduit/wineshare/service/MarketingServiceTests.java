//package com.keduit.wineshare.service;
//
//import com.keduit.wineshare.constant.EventOrNot;
//import com.keduit.wineshare.constant.MarketCategory;
//import com.keduit.wineshare.dto.MarketingDTO;
//import com.keduit.wineshare.entity.Marketing;
//import com.keduit.wineshare.repository.MarketingRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Transactional
//public class MarketingServiceTests {
//
//  @Autowired
//  private MarketingService marketingService;
//
//  @Autowired
//  private MarketingRepository marketingRepository;
//
//  @Test
//  @DisplayName("마케팅 등록 테스트")
//  public void saveMarketing() throws IOException{
//    MarketingDTO marketingDTO = new MarketingDTO();
//    marketingDTO.setMarketingTitle("테스트 업장명");
//    marketingDTO.setMarketingContent("테스트 업장 소개");
//    marketingDTO.setMarketCategory(MarketCategory.BOTTLESHOP);
//    marketingDTO.setEventOrNot(EventOrNot.END);
//    marketingDTO.setMarketImg("테스트 업장 사진");
//    marketingDTO.setMarketLink("테스트 업장 링크");
//
//    Long maketingId = marketingService.saveMarketing(marketingDTO);
//
//    Marketing marketing = marketingRepository.findById(maketingId).orElseThrow(EntityNotFoundException::new);
//
//    assertEquals(marketingDTO.getMarketingTitle(), marketing.getMarketingTitle());
//    assertEquals(marketingDTO.getMarketingContent(), marketing.getMarketingContent());
//    assertEquals(marketingDTO.getMarketCategory(), marketing.getMarketCategory());
//    assertEquals(marketingDTO.getMarketImg(), marketing.getMarketImg());
//    assertEquals(marketingDTO.getMarketLink(), marketing.getMarketLink());
//    assertEquals(marketingDTO.getEventOrNot(), marketing.getEventOrNot());
//
//  }
//
//  @Test
//  @DisplayName("마케팅 수정 테스트")
//  public void updateMarketing() throws IOException{
//    Marketing marketing = marketingRepository.findById(2L).orElseThrow(EntityNotFoundException::new);
//    marketing.setMarketingTitle("수정된 테스트 업장");
//
//    marketingRepository.save(marketing);
//    Marketing updateMarketing = marketingRepository.findById(2L).orElseThrow(EntityNotFoundException::new);
//
//    System.out.println(updateMarketing);
//
//  }
//
//}
