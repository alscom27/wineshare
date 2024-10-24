package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.BoardReplyDTO;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.BoardRepository;
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

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
public class BoardReplyController {

  private final BoardReplyService boardReplyService;
  private final MemberRepository memberRepository;
  private final BoardRepository boardRepository;



  // 댓글등록
  @PostMapping(value = "/new/{boardId}", consumes = "application/json", produces = "text/plain")
  public ResponseEntity<String> registerReply(@RequestBody BoardReplyDTO boardReplyDTO,
                                              @PathVariable("boardId") Long boardId,
                                              Principal principal){
    Member member = memberRepository.findByEmail(principal.getName());
    Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
    boardReplyDTO.setMemberId(member.getId());
    boardReplyDTO.setBoardId(board.getId());

    boardReplyService.registerReply(boardReplyDTO);
    return new ResponseEntity<>("댓글이 등록되었습니다.", HttpStatus.CREATED);
  }

  // 댓글 수정
  @PutMapping(value = "/{replyId}", consumes = "application/json", produces = "text/plain")
  public ResponseEntity<String> modifyReply(@PathVariable("replyId") Long replyId,
                                            @RequestBody BoardReplyDTO boardReplyDTO,
                                            Principal principal){
    if(!boardReplyService.validationBoardReply(replyId, principal.getName())){
      return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    boardReplyService.modifyReply(replyId, boardReplyDTO);
    return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
  }

  // 댓글 삭제
  @DeleteMapping(value = "/{replyId}", produces = "text/plain")
  public ResponseEntity<String> removeReply(@PathVariable("replyId") Long replyId,
                                            Principal principal){

    if(!boardReplyService.validationBoardReply(replyId, principal.getName())){
      return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    boardReplyService.remove(replyId);
    return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
  }

  // 특정 게시글 댓글 목록 가져오기
  @GetMapping(value = {"/{boardId}/{page}", "/{boardId}"}, produces = "application/json")
  public ResponseEntity<Map<String, Object>> getReplyWithPage(@PathVariable("boardId") Long boardId,
                                                              @PathVariable("page")Optional<Integer> page){
    Pageable pageable = PageRequest.of(page.orElse(0), 3);
    Page<BoardReplyDTO> replies = boardReplyService.getBoardReplyWithPage(boardId, pageable);


    Map<String, Object> response = new HashMap<>();
    response.put("boardReplys", replies.getContent());
    response.put("currentPage", replies.getNumber());
    response.put("totalPages", replies.getTotalPages());
    response.put("maxPage", 5);

    return ResponseEntity.ok(response);
  }

}
