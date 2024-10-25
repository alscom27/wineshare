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

  @Column(length = 2000)
  private String boardContent;

  //등업, 요청 사진o, // 질문, 공지 사진x
  private String boardImgName;   // 고유아이디 이미지 파일명

  private String boardOriImgName;  // 원본 이미지 이름

  private String boardImgUrl;  // 이미지 조회 경로

  @Enumerated(EnumType.STRING)
  private BoardStatus boardStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;


  public void updateBoard(BoardDTO boardDTO){
    this.boardTitle = boardDTO.getBoardTitle();
    this.boardContent = boardDTO.getBoardContent();
    this.boardStatus = boardDTO.getBoardStatus();

    if(this.boardImgName != null){
      this.boardImgName = boardDTO.getBoardImgName();
      this.boardImgUrl = boardDTO.getBoardImgUrl();
      this.boardOriImgName = boardDTO.getBoardOriImgName();
    }

  }

}
