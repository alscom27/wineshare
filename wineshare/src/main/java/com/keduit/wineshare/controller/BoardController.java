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

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 닉네입 세팅
    // regBy(이메일)로 작성자 정보를 조회
    List<String> emails = boards.stream()
        .map(Board::getRegBy) // regBy에서 이메일 가져오기
        .collect(Collectors.toList());

    List<Member> members = memberRepository.findByEmailIn(emails); // 이메일로 멤버 조회

    // 이메일을 기준으로 닉네임을 맵핑
    Map<String, String> emailToNicknameMap = members.stream()
        .collect(Collectors.toMap(Member::getEmail, Member::getNickname));

    // 닉네임을 추가할 DTO 리스트 생성
    List<BoardDTO> boardDTOs = boards.stream().map(board -> {
      BoardDTO boardDTO = new BoardDTO();
      // 기존 BoardDTO 필드 설정
      boardDTO.setId(board.getId());
      boardDTO.setBoardTitle(board.getBoardTitle());
      boardDTO.setBoardContent(board.getBoardContent());
      boardDTO.setWriterNickname(board.getRegBy());
      boardDTO.setCreateDate(board.getRegTime());
      boardDTO.setBoardStatus(board.getBoardStatus());
      // 닉네임 설정
      boardDTO.setWriterNickname(emailToNicknameMap.get(board.getRegBy())); // 닉네임 설정
      return boardDTO;
    }).collect(Collectors.toList());

    // ㅋㅋㅋㅋ진짜 존나돌아갔다..이거보다 쉬운게 있을텐데
    model.addAttribute("boardDTOs", boardDTOs);
    model.addAttribute("boards", boards);
    model.addAttribute("boardStatus", boardStatus);
    model.addAttribute("boardSearchDTO", boardSearchDTO);
    model.addAttribute("maxPage", 5);
    return "board/boardList";
  }

  @GetMapping({"/{boardStatus}/new"})
  public String boardForm(@PathVariable("boardStatus") BoardStatus boardStatus,
                          Model model){

    BoardDTO boardDTO = new BoardDTO();
//    boardDTO.setId(0L);
    boardDTO.setBoardStatus(boardStatus);

    model.addAttribute("boardDTO", boardDTO);
    model.addAttribute("boardStatus", boardStatus);
    return "board/boardForm";
  }

  @PostMapping({"/{boardStatus}/new"})
  public String boardNew(@PathVariable("boardStatus") BoardStatus boardStatus,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         Principal principal,
                         @RequestParam(value = "boardImgFile", required = false) MultipartFile boardImgFile,
                         Model model) throws IOException {
    if(bindingResult.hasErrors()){
      model.addAttribute("boardStatus", boardStatus);
      return "board/boardForm";
    }

    // 사용자 정보 가져오기
    String email = principal.getName();
    Member member = memberRepository.findByEmail(email);

    // 닉네임 설정
    boardDTO.setWriterNickname(member.getNickname());
    boardDTO.setMember(member);

    boardDTO.setBoardStatus(boardStatus);
    boardDTO.setBoardImgFile(boardImgFile);

    // 이미지 파일 처리
    // 파일 처리
    if (boardImgFile != null && !boardImgFile.isEmpty()) {
      // 파일이 선택된 경우 처리
      boardDTO.setBoardImgFile(boardImgFile);
      // 파일 저장 로직 추가 (예: 파일 시스템이나 데이터베이스에 저장)
    } else {
      // 파일이 선택되지 않은 경우 처리
      boardDTO.setBoardImgFile(null); // 또는 기본 이미지 설정
    }

    boardService.saveBoard(boardDTO);
    return "redirect:/boards/" + boardStatus + "/list/0";
  }

  // 상세보기
  @GetMapping({"/{boardStatus}/get/{boardId}"})
  public String boardDtl(@PathVariable("boardStatus") BoardStatus boardStatus,
                         @PathVariable("boardId") Long boardId,
                         Principal principal,
                         Model model){
    BoardDTO boardDTO = boardService.getBoardDtl(boardId);
    // 닉네임 세팅
    Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
    Member member = memberRepository.findByEmail(board.getRegBy());
    boardDTO.setWriterNickname(member.getNickname());

    // 현재 사용자의 이메일을 가져옵니다.
    String currentUserEmail = principal.getName();

    // 작성자와 비교하여 동일한지 확인
    boolean isAuthor = board.getMember().getEmail().equals(currentUserEmail);

    model.addAttribute("board", board);
    model.addAttribute("boardDTO", boardDTO);
    model.addAttribute("isAuthor", isAuthor);
    model.addAttribute("boardStatus", boardStatus);

    return "board/boardGetForm";
  }

  // 수정하기 겟매핑 추가?

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
