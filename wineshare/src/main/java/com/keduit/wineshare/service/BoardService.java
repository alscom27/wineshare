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
  private final ImgFileService imgFileService;

  // 게시판 상태에 따라 이미지 업로드 허용 여부 체크
  private boolean isImgUploadAllowed(BoardStatus boardStatus){
    return boardStatus == BoardStatus.LEVELUP || boardStatus == BoardStatus.REQUEST;
  }



  // 게시글저장
  public Long saveBoard(BoardDTO boardDTO) throws IOException {
    // 게시판 상태에 따라 이미지 파일 처리
    if (isImgUploadAllowed(boardDTO.getBoardStatus())){
      MultipartFile imageFile = boardDTO.getBoardImgFile();
      if(imageFile != null && !imageFile.isEmpty()){
        String imagePath = imgFileService.saveBoardImg(imageFile); // 파일 저장 로직
        boardDTO.setBoardImg(imagePath);
      }
    }else {
      // 이미지 파일을 받지 않을 경우, boardImg를 null로 설정
      boardDTO.setBoardImgFile(null);
    }

    // 게시글 등록
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

    // 이미지 처리
    MultipartFile imageFile = boardDTO.getBoardImgFile();
    if(imageFile != null && !imageFile.isEmpty()){
      // 이미지가 업로드 된경우 기존이미지를 업데이트
      String imagePath = imgFileService.saveBoardImg(imageFile);
      boardDTO.setBoardImg(imagePath);  // 새로운 이미지 경로 설정
    }else{
      // 기존이미지 유지
      boardDTO.setBoardImg(board.getBoardImg());
    }

    board.updateBoard(boardDTO);
    return board.getId();
  }

  // 게시글 삭제
  public void deleteBoard(Long boardId){
    boardRepository.deleteById(boardId);
  }

  @Transactional(readOnly = true)
  public Page<Board> getBoardPageByStatus(BoardSearchDTO boardSearchDTO, BoardStatus boardStatus, Pageable pageable){
    return boardRepository.getBoardPage(boardSearchDTO, boardStatus, pageable);
  }

}
