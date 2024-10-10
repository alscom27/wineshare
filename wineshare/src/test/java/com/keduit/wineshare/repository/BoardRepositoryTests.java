package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
public class BoardRepositoryTests {

  @Autowired
  BoardRepository boardRepository;

  @PersistenceContext
  EntityManager em;

  public void createBoardList(){
    for(int i=1; i<=10; i++){
      Board board = new Board();
      board.setBoardTitle("테스트 제목" + i);
      board.setBoardContent("테스트 내용" + i);
      boardRepository.save(board);
    }
  }

  @Test
  @DisplayName("게시글 저장 테스트")
  public void createBoardTest(){
    Board board = new Board();
    board.setBoardTitle("테스트 제목");
    board.setBoardContent("테스트 내용");
    board.setBoardStatus(BoardStatus.QUESTION);

    Board saveBoard = boardRepository.save(board);

    System.out.println(saveBoard);
  }

  @Test
  @DisplayName("Querydsl 게시판 조회 테스트")
  public void querydslTest(){
    this.createBoardList();
    QBoard board = QBoard.board;
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    String title ="테스트";

    booleanBuilder.and(board.boardTitle.like("%" + title + "%"));


    Pageable pageable = PageRequest.of(0, 10);
    Page<Board> boardPageResult = boardRepository.findAll(booleanBuilder, pageable);

    System.out.println("total board : " + boardPageResult);

    List<Board> resultBoardList = boardPageResult.getContent();
    resultBoardList.forEach(System.out::println);
  }

}
