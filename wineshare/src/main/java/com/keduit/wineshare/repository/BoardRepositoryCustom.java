package com.keduit.wineshare.repository;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.dto.BoardSearchDTO;
import com.keduit.wineshare.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

  // 게시글 전체 조회는 없고 게시글 상태별로 조회
  Page<Board> getBoardPage(BoardSearchDTO boardSearchDTO, BoardStatus boardStatus, Pageable pageable);

}