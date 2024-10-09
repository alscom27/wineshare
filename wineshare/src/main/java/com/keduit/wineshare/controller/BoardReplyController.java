package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.BoardReplyDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.BoardReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("replies")
@RequiredArgsConstructor
public class BoardReplyController {

  private final BoardReplyService boardReplyService;
  private final MemberRepository memberRepository;



  // 댓글등록
  @PostMapping(value = "/new", consumes = "application/json", produces = "text/plain")
  public ResponseEntity<String> registerReply(@RequestBody BoardReplyDTO boardReplyDTO,
                                              Principal principal){
    Member member = memberRepository.findByEmail(principal.getName());
    boardReplyDTO.setUserId(member.getId());

    boardReplyService.registerReply(boardReplyDTO);
    return new ResponseEntity<>("댓글이 등록되었습니다.", HttpStatus.CREATED);
  }

  // 댓글 수정
  @PutMapping(value = "/{replyId}", consumes = "application/json", produces = "text/plain")
  public ResponseEntity<String> modifyReply(@PathVariable("replyId") Long replyId,
                                            @RequestBody BoardReplyDTO boardReplyDTO){
    boardReplyService.modifyReply(replyId, boardReplyDTO);
    return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
  }

  // 댓글 삭제
  @DeleteMapping(value = "/{replyId}", produces = "text/plain")
  public ResponseEntity<String> removeReply(@PathVariable("replyId") Long replyId){
    boardReplyService.remove(replyId);
    return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
  }

  // 특정 게시글 댓글 목록 가져오기
  @GetMapping(value = "/{boardId}/{page}", produces = "application/json")
  public ResponseEntity<Page<BoardReplyDTO>> getReplyWithPage(@PathVariable("boardId") Long boardId,
                                                              @PathVariable("page")Optional<Integer> page){
    Pageable pageable = PageRequest.of(page.orElse(0), 5);
    Page<BoardReplyDTO> replies = boardReplyService.getBoardReplyWithPage(boardId, pageable);
    return new ResponseEntity<>(replies, HttpStatus.OK);
  }

}
