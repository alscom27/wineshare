package com.keduit.wineshare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.Member;
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

  private String boardImg;  // 저장된 이미지 경로

  private MultipartFile boardImgFile; // 업로드할 이미지파일

  private String writerNickname;

  private Member member;

  @JsonFormat(pattern = "yy-MM-dd")
  private LocalDateTime createDate;

  public BoardDTO(){}

  private static ModelMapper modelMapper = new ModelMapper();

  public Board createBoard(){
    return modelMapper.map(this, Board.class);
  }

  public static BoardDTO of(Board board){
    return modelMapper.map(board, BoardDTO.class);
  }

}
