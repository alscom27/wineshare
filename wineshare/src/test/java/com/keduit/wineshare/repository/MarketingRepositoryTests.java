//package com.keduit.wineshare.repository;
//
//import com.keduit.wineshare.constant.EventOrNot;
//import com.keduit.wineshare.constant.MarketCategory;
//import com.keduit.wineshare.entity.Marketing;
//import com.keduit.wineshare.entity.QMarketing;
//import com.querydsl.core.BooleanBuilder;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.List;
//
//@SpringBootTest
//public class MarketingRepositoryTests {
//
//  @Autowired
//  MarketingRepository marketingRepository;
//
//  @PersistenceContext
//  EntityManager em;
//
//  public void createMarketingList(){
//    for(int i=1; i<=10; i++){
//      Marketing marketing = new Marketing();
//      marketing.setMarketingTitle("테스트 업장" + i);
//      marketing.setMarketingContent("테스트 업장 설명" + i);
//      marketing.setMarketCategory(MarketCategory.BISTRO);
//      if(i%2==0){
//        marketing.setEventOrNot(EventOrNot.END);
//      }else{
//        marketing.setEventOrNot(EventOrNo.ON);
//        marketing.setCheckContent("행사 내용" + i);
//      }
//      marketing.setMarketImg("업장사진" + i);
//      marketing.setMarketLink("업장링크" + i);
//      marketingRepository.save(marketing);
//    }
//  }
//
//  @Test
//  @DisplayName("마케팅 저장 테스트")
//  public void createMarketingTest(){
//    Marketing marketing = new Marketing();
//    marketing.setMarketingTitle("테스트 업장");
//    marketing.setMarketingContent("테스트 업장 설명");
//    marketing.setMarketCategory(MarketCategory.WINEBAR);
//    marketing.setEventOrNot(EventOrNot.ON);
//    marketing.setCheckContent("행사 내용");
//    marketing.setMarketImg("업장 사진");
//    marketing.setMarketLink("업장 링크");
//
//    Marketing saveMarketing = marketingRepository.save(marketing);
//
//    System.out.println(saveMarketing);
//  }
//
//  @Test
//  @DisplayName("Querydsl 마케팅 조회 테스트")
//  public void querydslTest(){
//    this.createMarketingList();
//    QMarketing marketing = QMarketing.marketing;
//    BooleanBuilder booleanBuilder = new BooleanBuilder();
//
//    String title = "테스트";
//
//    booleanBuilder.and(marketing.marketingTitle.like("%" + title + "%"));
//
//    Pageable pageable = PageRequest.of(0,10);
//    Page<Marketing> marketingPageResult = marketingRepository.findAll(booleanBuilder, pageable);
//
//    System.out.println("total marketing" + marketingPageResult);
//
//    List<Marketing> resultMarketingList = marketingPageResult.getContent();
//    resultMarketingList.forEach(System.out::println);
//  }
//
//}
