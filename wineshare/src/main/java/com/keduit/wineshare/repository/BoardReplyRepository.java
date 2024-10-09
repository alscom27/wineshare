package com.keduit.wineshare.repository;

import com.keduit.wineshare.entity.BoardReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

  Page<BoardReply> findByBoardId(Long boardId, Pageable pageable);

}
