package com.keduit.wineshare.service;

import com.keduit.wineshare.dto.BoardReplyDTO;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.BoardReply;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.BoardReplyRepository;
import com.keduit.wineshare.repository.BoardRepository;
import com.keduit.wineshare.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BoardReplyServiceTests {

  @Autowired
  private BoardReplyService boardReplyService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private BoardReplyRepository boardReplyRepository;

  @Test
  @DisplayName("댓글 조회 테스트")
  public void testGetListPage(){
    Member member = new Member();
    member.setName("testUser");
    member.setPassword("12345678");
    memberRepository.save(member);

    Board board = new Board();
    board.setBoardTitle("test board");
    board.setBoardContent("test board content");
    boardRepository.save(board);

    for(int i=0; i<10; i++){
      BoardReply reply = new BoardReply();
      reply.setReply("test reply " + i);
      reply.setUser(member);
      reply.setBoard(board);
      boardReplyRepository.save(reply);
    }

    Pageable pageable = PageRequest.of(0, 5);
    List<BoardReplyDTO> replyDTOList = boardReplyService.getBoardReplyWithPage(board.getId(), pageable).getContent();

    System.out.println(replyDTOList.get(0).toString());

    assertThat(replyDTOList).hasSize(5);  // 첫페이지 5개의 댓글이 조회되어야함
    assertThat(replyDTOList.get(0).getReply()).isEqualTo("test reply 0"); // 첫번째 댓글내용확인

  }


}
