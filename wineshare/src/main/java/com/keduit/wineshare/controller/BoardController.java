package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.dto.BoardDTO;
import com.keduit.wineshare.dto.BoardSearchDTO;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.BoardRepository;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;


  // 기본 0페이지도 주소 추가하기 하.. 할거 존나많네 주석보면서 이런말 보이면 지워야됨
  @GetMapping({"/{boardStatus}/list/{page}", "/{boardStatus}/list"})
  public String getBoardListByStatus(@PathVariable("boardStatus")BoardStatus boardStatus,
                                     @PathVariable("page") Optional<Integer> page,
                                     BoardSearchDTO boardSearchDTO,
                                     Model model){


    Pageable pageable = PageRequest.of(page.orElse(0), 5);

    Page<Board> boards = boardService.getBoardPageByStatus(boardSearchDTO, boardStatus, pageable);

    model.addAttribute("boards", boards);
    model.addAttribute("boardStatus", boardStatus);
    model.addAttribute("boardSearchDTO", boardSearchDTO);
    model.addAttribute("maxPage", 5);
    return "board/boardList";
  }

  @GetMapping({"/{boardStatus}/new"})
  public String boardForm(@PathVariable("boardStatus") BoardStatus boardStatus,
                          Model model){

    model.addAttribute("boardDTO", new BoardDTO());
    return "board/boardForm";
  }

  @PostMapping({"/{boardStatus}/new"})
  public String boardNew(@PathVariable("boardStatus") BoardStatus boardStatus,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         Principal principal,
                         @RequestParam("boardImg") MultipartFile boardImg,
                         Model model) throws IOException {
    if(bindingResult.hasErrors()){
      return "redirect:/";
    }

    // 사용자 정보 가져오기
    Member member = memberRepository.findByEmail(principal.getName());

    // 닉네임 설정
    boardDTO.setWriterNickname(member.getNickname());
    boardDTO.setBoardStatus(boardStatus);

    boardService.saveBoard(boardDTO);
    return "redirect:/boards/" + boardStatus + "/0";
  }

  // 상세보기
  @GetMapping({"/{boardStatus}/get/{boardId}"})
  public String boardDtl(@PathVariable("boardStatus") BoardStatus boardStatus,
                         @PathVariable("boardId") Long boardId,
                         Model model){
    BoardDTO boardDTO = boardService.getBoardDtl(boardId);
    model.addAttribute("boardDTO", boardDTO);
    return "board/boardForm";
  }

  @PostMapping({"/{boardStatus}/modify/{boardId}"})
  public String boardUpdate(@PathVariable("boardStatus") BoardStatus boardStatus,
                            @PathVariable("boardId") Long boardId,
                            @Valid BoardDTO boardDTO,
                            BindingResult bindingResult,
                            @RequestParam("boardImg") MultipartFile boardImg,
                            Model model) throws  IOException{
    if(bindingResult.hasErrors()){
      return "board/boardForm";
    }

    boardDTO.setId(boardId);  //수정할 게시글
    boardDTO.setBoardImgFile(boardImg); // 업로드할 이미지 파일 설정

    boardService.updateBoard(boardDTO);
    return "redirect:/boards/" + boardStatus + "/0";
  }

  @PostMapping({"/{boardStatus}/remove/{boardId}"})
  public String boardDelete(@PathVariable("boardStatus") BoardStatus boardStatus,
                            @PathVariable("boardId") Long boardId){

    boardService.deleteBoard(boardId);
    return "redirect:/boards/" + boardStatus + "/0";
  }

}
