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

  private String board_title;

  private String board_content;

  private String boardImg;  //등업, 요청 사진

  @Enumerated
  private BoardStatus board_status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

}
