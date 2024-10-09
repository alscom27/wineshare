package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.dto.BoardDTO;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BoardServiceTests {

  @Autowired
  private BoardService boardService;

  @Autowired
  private BoardRepository boardRepository;

  @Test
  @DisplayName("게시글 등록 테스트")
  public void saveBoard() throws IOException {
    BoardDTO boardDTO = new BoardDTO();
    boardDTO.setBoardTitle("테스트 게시글 제목");
    boardDTO.setBoardContent("테스트 게시글 내용");
    boardDTO.setWriterNickname("테스트 게시글 작성자");
    boardDTO.setBoardStatus(BoardStatus.QUESTION);

    Long boardId = boardService.saveBoard(boardDTO);

    Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

    assertEquals(boardDTO.getBoardTitle(), board.getBoardTitle());
    assertEquals(boardDTO.getBoardContent(), board.getBoardContent());
    //assertEquals(boardDTO.getWriterNickname(), board.getRegBy());

  }

}
