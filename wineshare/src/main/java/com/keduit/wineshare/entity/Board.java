package com.keduit.wineshare.entity;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Board extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id;

  private String boardTitle;

  private String boardContent;

  private String boardImg;  //등업, 요청 사진o, // 질문, 공지 사진x

  @Enumerated
  private BoardStatus boardStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  public void updateBoard(BoardDTO boardDTO){
    this.boardTitle = boardDTO.getBoardTitle();
    this.boardContent = boardDTO.getBoardContent();
    this.boardStatus = boardDTO.getBoardStatus();

    if(this.boardImg != null){
      this.boardImg = boardDTO.getBoardImg();
    }

  }

}
