package com.keduit.wineshare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardDTO {

  private Long id;

  @NotBlank(message = "제목은 필수 입력입니다.")
  private String boardTitle;

  @NotBlank(message = "내용은 필수 입력입니다.")
  private String boardContent;

  private BoardStatus boardStatus;

  //등업, 요청 사진o, // 질문, 공지 사진x
  private String boardImgName;   // 고유아이디 이미지 파일명

  private String boardOriImgName;  // 원본 이미지 이름

  private String boardImgUrl;  // 이미지 조회 경로

  private String writerNickname;

  private String regBy;

  @JsonFormat(pattern = "yy-MM-dd")
  private LocalDateTime regTime;

  private Member member;

  public BoardDTO(){}

  private static ModelMapper modelMapper = new ModelMapper();

  public Board createBoard(){
    return modelMapper.map(this, Board.class);
  }

  public static BoardDTO of(Board board){
    return modelMapper.map(board, BoardDTO.class);
  }

  @QueryProjection
  public BoardDTO(Long id, BoardStatus boardStatus, String boardTitle, String regBy, LocalDateTime regTime, String writerNickname) {
    this.id = id;
    this.boardStatus = boardStatus;
    this.boardTitle = boardTitle;
    this.regBy = regBy;
    this.regTime = regTime;
    this.writerNickname = writerNickname;
  }

}
