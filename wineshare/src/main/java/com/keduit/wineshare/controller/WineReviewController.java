package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.WineReviewDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.repository.WineRepository;
import com.keduit.wineshare.repository.WineReviewRepository;
import com.keduit.wineshare.service.WineReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Log4j2
public class WineReviewController {

  private final WineReviewRepository wineReviewRepository;
  private final WineReviewService wineReviewService;
  private final WineRepository wineRepository;
  private final MemberRepository memberRepository;

  // 리뷰 등록
  @PostMapping(value = "/new/{wineId}", consumes = "application/json", produces = "text/plain")
  public ResponseEntity<String> registerReview(@RequestBody WineReviewDTO wineReviewDTO,
                                               @PathVariable("wineId") Long wineId,
                                               Principal principal){
    Member member = memberRepository.findByEmail(principal.getName());
    Wine wine = wineRepository.findById(wineId).orElseThrow(EntityNotFoundException::new);
    wineReviewDTO.setMember(member);
    wineReviewDTO.setWine(wine);


    wineReviewService.registerReview(wineReviewDTO);
    return new ResponseEntity<>("리뷰가 등록되었습니다.", HttpStatus.CREATED);
  }

  // 리뷰 수정
  @PutMapping(value = "/{reviewId}", consumes = "application/json", produces = "text/plain")
  public ResponseEntity<String> modifyReview(@PathVariable("reviewId") Long reviewId,
                                             @RequestBody WineReviewDTO wineReviewDTO,
                                             Principal principal){
    if(!wineReviewService.validationWineReview(reviewId, principal.getName())){
      return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }


    wineReviewService.modifyReview(reviewId, wineReviewDTO);
    return new ResponseEntity<>("리뷰가 수정되었습니다.", HttpStatus.OK);
  }

  // 리뷰 삭제
  @DeleteMapping(value = "/{reviewId}", produces = "text/plain")
  public ResponseEntity<String> removeReview(@PathVariable("reviewId") Long reviewId,
                                             Principal principal){

    if(!wineReviewService.validationWineReview(reviewId, principal.getName())){
      return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
    wineReviewService.remove(reviewId);
    return new ResponseEntity<>("리뷰가 삭제되었습니다.", HttpStatus.OK);
  }

  // 특정 와인 상세 리뷰 목록 가져오기
  @GetMapping({"/{wineId}", "/{wineId}/{reviewPage}"})
  public ResponseEntity<Map<String, Object>> getWineReviewList(@PathVariable("wineId") Long wineId,
                                                               @PathVariable("reviewPage") Optional<Integer> reviewPage){
    Pageable pageable = PageRequest.of(reviewPage.orElse(0), 3);
    Page<WineReviewDTO> reviews = wineReviewService.getReviewPageByWine(wineId, pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("wineReviews", reviews.getContent());
    response.put("currentPage", reviews.getNumber());
    response.put("totalPages", reviews.getTotalPages());
    response.put("maxPage", 5);

    return ResponseEntity.ok(response);
  }


}
