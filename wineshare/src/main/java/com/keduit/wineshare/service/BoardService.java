package com.keduit.wineshare.service;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.dto.BoardDTO;
import com.keduit.wineshare.dto.BoardSearchDTO;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
//  private final ImgFileService imgFileService;

  // 게시판 상태에 따라 이미지 업로드 허용 여부 체크
  private boolean isImgUploadAllowed(BoardStatus boardStatus){
    return boardStatus == BoardStatus.UPGRADE || boardStatus == BoardStatus.REQUEST;
  }



  // 게시글저장
  public Long saveBoard(BoardDTO boardDTO) throws IOException {


    Board board = boardDTO.createBoard();
    boardRepository.save(board);
    return board.getId();
  }

  // 게시글 상세 조회
  @Transactional(readOnly = true)
  public BoardDTO getBoardDtl(Long boardId){
    Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
    BoardDTO boardDTO = BoardDTO.of(board);

    return boardDTO;
  }

  // 게시글 업데이트
  public Long updateBoard(BoardDTO boardDTO) throws IOException {
    Board board = boardRepository.findById(boardDTO.getId())
        .orElseThrow(EntityNotFoundException::new);

    // 이미지 필드 설정
    board.setBoardImgName(boardDTO.getBoardImgName());
    board.setBoardImgUrl(boardDTO.getBoardImgUrl());
    board.setBoardOriImgName(boardDTO.getBoardOriImgName());

    board.updateBoard(boardDTO);

    System.out.println("======== service===========");
    System.out.println(board.getId());
    System.out.println(board.toString());
    System.out.println(boardDTO.toString());
    System.out.println("===================");

    return board.getId();
  }

  // 게시글 삭제
  public void deleteBoard(Long boardId){
    boardRepository.deleteById(boardId);
  }

  // 게시글 상태에 따른 페이지와  조회
  @Transactional(readOnly = true)
  public Page<Board> getBoardPageByStatus(BoardSearchDTO boardSearchDTO, BoardStatus boardStatus, Pageable pageable){
    return boardRepository.getBoardPageByStatus(boardSearchDTO, boardStatus, pageable);
  }

  // 관리자용 전체 게시글목록
  @Transactional(readOnly = true)
  public Page<Board> getBoardPage(BoardSearchDTO boardSearchDTO, Pageable pageable){
    return boardRepository.getBoardPage(boardSearchDTO, pageable);
  }

}
