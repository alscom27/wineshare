package com.keduit.wineshare.service;

import com.keduit.wineshare.dto.BoardReplyDTO;
import com.keduit.wineshare.entity.BoardReply;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.WineReview;
import com.keduit.wineshare.repository.BoardReplyRepository;
import com.keduit.wineshare.repository.BoardRepository;
import com.keduit.wineshare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReplyService {

  private final BoardReplyRepository boardReplyRepository;
  private final MemberRepository memberRepository;
  private final BoardRepository boardRepository;

  // 댓글 등록
  public void registerReply(BoardReplyDTO boardReplyDTO){
    BoardReply boardReply = new BoardReply();
    boardReply.setId(0L);
    boardReply.setReply(boardReplyDTO.getReply());
    boardReply.setMember(memberRepository.findById(boardReplyDTO.getMemberId()).orElseThrow(EntityNotFoundException::new));
    boardReply.setBoard(boardRepository.findById(boardReplyDTO.getBoardId()).orElseThrow(EntityNotFoundException::new));

    boardReplyRepository.save(boardReply);
  }

  // 게시글에 대한 댓글 목록조회(페이지)
  public Page<BoardReplyDTO> getBoardReplyWithPage(Long boardId, Pageable pageable){
    Page<BoardReply> replies = boardReplyRepository.findByBoardId(boardId, pageable);

    return replies.map(reply -> {
      BoardReplyDTO dto = new BoardReplyDTO();
      dto.setId(reply.getId());
      dto.setReply(reply.getReply());
      dto.setMemberId(reply.getMember().getId());
      dto.setBoardId(reply.getBoard().getId());
      dto.setRegTime(reply.getRegTime());
      dto.setReplyNickname(reply.getMember().getNickname());
      dto.setUpdateTime(reply.getUpdateTime());
      return dto;
    });
  }

  // 권한확인(작성자==로그인유저)
  @Transactional(readOnly = true)
  public boolean validationBoardReply(Long replyId, String email) {
    Member member = memberRepository.findByEmail(email);
    BoardReply boardReply = boardReplyRepository.findById(replyId).orElseThrow(EntityNotFoundException::new);
    Member savedMember = boardReply.getMember();
    if (!StringUtils.equals(member.getEmail(), savedMember.getEmail())) {
      return false;
    }
    return true;
  }

  // 댓글 수정
  public void modifyReply(Long replyId, BoardReplyDTO boardReplyDTO){
    BoardReply reply = boardReplyRepository.findById(replyId)
        .orElseThrow(EntityNotFoundException::new);

    reply.setReply(boardReplyDTO.getReply());

    boardReplyRepository.save(reply);
  }

  // 댓글 삭제
  public void remove(Long replyId){
    boardReplyRepository.deleteById(replyId);
  }


}
