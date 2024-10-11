package com.keduit.wineshare.service;

import com.keduit.wineshare.dto.WineReviewDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.entity.WineReview;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.repository.WineRepository;
import com.keduit.wineshare.repository.WineReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class WineReviewServiceTests {

  @Autowired
  private WineReviewService wineReviewService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private WineRepository wineRepository;

  @Autowired
  private WineReviewRepository wineReviewRepository;

  @Test
  @DisplayName("리뷰 조회 테스트")
  public void testGetListPage(){
    Member member = new Member();
    member.setName("testUser2");
    member.setPassword("12345678");
    memberRepository.save(member);

    Wine wine = wineRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

    for(int i=0; i<10; i++){
      WineReview review = new WineReview();
      review.setRegularReview("test review " + i);
      review.setRegularRating(3.0);
      review.setWine(wine);
      review.setMember(member);
      wineReviewRepository.save(review);
    }

    Pageable pageable = PageRequest.of(0, 5);
    List<WineReviewDTO> reviewDTOList = wineReviewService.getWineReviewWithPage(wine.getId(), pageable).getContent();

    System.out.println(reviewDTOList.get(0).toString());

    assertThat(reviewDTOList).hasSize(5);
    assertThat(reviewDTOList.get(0).getRegularReview().equals("test review 0"));

  }

}
