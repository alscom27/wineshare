package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.WineReviewDTO;
import com.keduit.wineshare.entity.Member;
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

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
@Log4j2
public class WineReviewController {

  private final WineReviewRepository wineReviewRepository;
  private final WineReviewService wineReviewService;
  private final WineRepository wineRepository;
  private final MemberRepository memberRepository;

  // 리뷰 등록
  @PostMapping(value = "/new", consumes = "application/json", produces = "text/plain")
  public ResponseEntity<String> registerReview(@RequestBody WineReviewDTO wineReviewDTO,
                                               Principal principal){
    Member member = memberRepository.findByEmail(principal.getName());
    wineReviewDTO.setMember(member);

    // 근데 게시판댓글도 그렇고 여기서 해당 게시글 또는 와인은 어떻게 세팅?
    wineReviewService.registerReview(wineReviewDTO);
    return new ResponseEntity<>("리뷰가 등록되었습니다.", HttpStatus.CREATED);
  }

  // 리뷰 수정
  @PutMapping(value = "/{reviewId}", consumes = "application/json", produces = "text/plain")
  public ResponseEntity<String> modifyReview(@PathVariable("reviewId") Long reviewId,
                                             @RequestBody WineReviewDTO wineReviewDTO){
    wineReviewService.modifyReview(reviewId, wineReviewDTO);
    return new ResponseEntity<>("리뷰가 수정되었습니다.", HttpStatus.OK);
  }

  // 댓글 삭제
  @DeleteMapping(value = "/{reviewId}", produces = "text/plain")
  public ResponseEntity<String> removeReview(@PathVariable("reviewId") Long reviewId){
    wineReviewService.remove(reviewId);
    return new ResponseEntity<>("리뷰가 삭제되었습니다.", HttpStatus.OK);
  }

  // 특정 와인 상세 리뷰 목록 가져오기
  @GetMapping(value = "/{wineId}/{page}", produces = "application/json")
  public ResponseEntity<Page<WineReviewDTO>> getReviewWithPage(@PathVariable("wineId") Long wineId,
                                                               @PathVariable("page")Optional<Integer> page){
    Pageable pageable = PageRequest.of(page.orElse(0), 5);
    Page<WineReviewDTO> reviews = wineReviewService.getWineReviewWithPage(wineId, pageable);
    return new ResponseEntity<>(reviews, HttpStatus.OK);

  }


//  // 임시
//  @GetMapping("/wineReview")
//  public String list(){
//    log.info("wineReview");
//    return "wine/wineReview";
//  }

}
