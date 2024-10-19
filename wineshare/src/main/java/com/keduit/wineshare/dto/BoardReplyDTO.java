package com.keduit.wineshare.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardReplyDTO {

  private Long id;

  private String reply; // 댓글 내용

  private Long memberId;

  private String replyNickname;

  private Long boardId;

  private LocalDateTime regTime;

  private LocalDateTime updateTime;

}
