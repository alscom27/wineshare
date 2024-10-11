package com.keduit.wineshare.service;

import com.keduit.wineshare.dto.WineReviewDTO;
import com.keduit.wineshare.entity.WineReview;
import com.keduit.wineshare.repository.WineReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class WineReviewService {

  private final WineReviewRepository wineReviewRepository;

  // 리뷰 등록
  public void registerReview(WineReviewDTO wineReviewDTO){
    WineReview wineReview = new WineReview();
    wineReview.setRegularReview(wineReviewDTO.getRegularReview());
    wineReview.setRegularRating(wineReviewDTO.getRegularRating());

    // 우선 멤버는 principal로 보드는 패스배리어블로 될거라 믿는다

    wineReviewRepository.save(wineReview);
  }

  // 와인상세에 대한 리뷰 목록조회(페이지)
  public Page<WineReviewDTO> getWineReviewWithPage(Long wineId, Pageable pageable){
    Page<WineReview> reviews = wineReviewRepository.findByWineId(wineId, pageable);

    return reviews.map(review -> {
      WineReviewDTO dto = new WineReviewDTO();
      dto.setId(review.getId());
      dto.setRegularReview(review.getRegularReview());
      dto.setRegularRating(review.getRegularRating());
      // 여기서부턴 의식의 흐름대로...
      dto.setMember(review.getMember());
      dto.setWine(review.getWine());
      dto.setRegTime(review.getRegTime());
      dto.setUpdateTime(review.getUpdateTime());
      return dto;
    });
  }

  // 리뷰 수정
  public void modifyReview(Long reviewId, WineReviewDTO wineReviewDTO){
    WineReview review = wineReviewRepository.findById(reviewId)
        .orElseThrow(EntityNotFoundException::new);

    review.setRegularReview(wineReviewDTO.getRegularReview());
    review.setRegularRating(wineReviewDTO.getRegularRating());

    wineReviewRepository.save(review);
  }

  // 리뷰 삭제
  public void remove(Long reviewId){
    wineReviewRepository.deleteById(reviewId);
  }

}
