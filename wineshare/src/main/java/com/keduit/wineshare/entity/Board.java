package com.keduit.wineshare.entity;

import com.keduit.wineshare.constant.BoardStatus;
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

  private String boardImg;  //등업, 요청 사진

  @Enumerated
  private BoardStatus boardStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Member user;

}
