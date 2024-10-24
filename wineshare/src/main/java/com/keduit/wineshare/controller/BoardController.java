package com.keduit.wineshare.controller;

import com.keduit.wineshare.constant.BoardStatus;
import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.dto.BoardDTO;
import com.keduit.wineshare.dto.BoardSearchDTO;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.repository.BoardRepository;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.BoardService;
import com.keduit.wineshare.service.ImgFileService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
  private final ImgFileService imgFileService;


  @GetMapping({"/{boardStatus}/list/{page}", "/{boardStatus}/list"})
  public String getBoardListByStatus(@PathVariable("boardStatus")BoardStatus boardStatus,
                                     @PathVariable("page") Optional<Integer> page,
                                     BoardSearchDTO boardSearchDTO,
                                     Principal principal,
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
      boardDTO.setRegTime(board.getRegTime());
      boardDTO.setBoardStatus(board.getBoardStatus());
      boardDTO.setMember(board.getMember());
      // 닉네임 설정
      boardDTO.setWriterNickname(emailToNicknameMap.get(board.getRegBy())); // 닉네임 설정
      return boardDTO;
    }).collect(Collectors.toList());

    // 어드민인지(로그인 한 사람이)
    boolean isAdmin = principal instanceof KafkaProperties.Admin;

    // 위에 게시글을 작성한 유저의 멤버타입이 expert인지 확인해서 보여줄 수 있는방법

    model.addAttribute("isAdmin", isAdmin);
    model.addAttribute("members", members);
    model.addAttribute("boardDTOs", boardDTOs);
    model.addAttribute("boards", boards);
    model.addAttribute("boardStatus", boardStatus);
    model.addAttribute("boardSearchDTO", boardSearchDTO);
    model.addAttribute("maxPage", 5);
    return "board/boardList";
  }

  // 게시글 등록버튼시 로그인 여부 땜에 만듬
  @GetMapping("/isLoggedIn")
  public ResponseEntity<Boolean> isLoggedIn(Principal principal) {
    boolean isLoggedIn = principal != null;
    return ResponseEntity.ok(isLoggedIn);
  }

  @GetMapping({"/{boardStatus}/new"})
  public String boardForm(@PathVariable("boardStatus") BoardStatus boardStatus,
                          Principal principal,
                          Model model){





    BoardDTO boardDTO = new BoardDTO();
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
      // 에러메시지 보내는지 확인해보자
      model.addAttribute("boardStatus", boardStatus);
      return "board/boardForm";
    }

    // 사용자 정보 가져오기
    String email = principal.getName();
    Member member = memberRepository.findByEmail(email);

    // 닉네임 설정
    boardDTO.setWriterNickname(member.getNickname());

    // 멤버 넣어야함
    boardDTO.setMember(member);


    boardDTO.setBoardStatus(boardStatus);


    // 이미지 파일 처리
    // 파일 처리
    if(StringUtils.equalsIgnoreCase(boardStatus, "upgrade") || StringUtils.equalsIgnoreCase(boardStatus, "request")){
      if (boardImgFile != null && !boardImgFile.isEmpty()) {
        try {
          // 파일이 선택된 경우 처리
          imgFileService.saveBoardImg(boardDTO, boardImgFile);
        }catch (Exception e){
          // 파일이 선택되지 않은 경우 처리
          model.addAttribute("errorMessage", "사진은 필수야 시발아");
          return "board/boardForm";
        }
      }
    }else if(StringUtils.equalsIgnoreCase(boardStatus, "notice") || StringUtils.equalsIgnoreCase(boardStatus, "question")){
      boardDTO.setBoardOriImgName(null);
      boardDTO.setBoardImgName(null);
      boardDTO.setBoardImgUrl(null);
    }



    boardService.saveBoard(boardDTO);
    return "redirect:/boards/" + boardStatus + "/list";
  }

  // 상세보기
  @GetMapping({"/{boardStatus}/get/{boardId}"})
  public String boardDtl(@PathVariable("boardStatus") BoardStatus boardStatus,
                         @PathVariable("boardId") Long boardId,
                         Principal principal,
                         Model model,
                         RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {

    // 로그인 여부 확인 게시글 상세보기 안되고 로그인으로 보내라고함
    if(boardStatus != BoardStatus.NOTICE) {
      if (principal == null) {
        // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        model.addAttribute("errorMsg", "로그인 후 이용해주세요.");
        return "redirect:/member/memberLoginForm"; // 로그인 페이지 URL
      }
    }
    BoardDTO boardDTO = boardService.getBoardDtl(boardId);
    // 닉네임 세팅
    Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
    Member member = memberRepository.findByEmail(board.getRegBy());
    boardDTO.setWriterNickname(member.getNickname());
    boardDTO.setId(board.getId());

    // 현재 사용자의 이메일을 가져옵니다.

    String currentUserEmail = "";
    if(principal != null){
      currentUserEmail = principal.getName();
    }

    // 작성자와 비교하여 동일한지 확인
    boolean isAuthor = member.getEmail().equals(currentUserEmail);

    // 게시글의 작성자가 현재 유저인지
    boolean isWriterRegular = member.getMemberType().equals(MemberType.REGULAR);

    // 등업 게시글이면 다른작성자가 상세보기누르면 경고
    if(boardStatus == BoardStatus.UPGRADE && !isAuthor) {
      if (memberRepository.findByEmail(principal.getName()).getMemberType() != MemberType.ADMIN) {
//      model.addAttribute("errorMessage", "에러 표시해");
        redirectAttributes.addFlashAttribute("errorMessage", "이 글은 작성자 본인만 조회할 수 있습니다.");
        return "redirect:/boards/" + boardStatus + "/list";
      }
    }
    Member curruntMember = new Member();
    if(principal != null) {
      curruntMember = memberRepository.findByEmail(principal.getName());
    }
    model.addAttribute("loginUser", curruntMember);
    model.addAttribute("isWriterRegular", isWriterRegular);
    model.addAttribute("memberId", member.getId());
    model.addAttribute("board", board);
    model.addAttribute("boardDTO", boardDTO);
    model.addAttribute("isAuthor", isAuthor);
    model.addAttribute("boardStatus", boardStatus);

    return "board/boardGetForm";
  }

  // 수정하기 겟매핑 추가
  @GetMapping("/{boardStatus}/modify/{boardId}")
  public String boardUpdate(@PathVariable("boardStatus") BoardStatus boardStatus,
                            @PathVariable("boardId") Long boardId,
                            Model model){
    BoardDTO boardDTO = boardService.getBoardDtl(boardId);

    // 작성자의 닉네임을 설정
    Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
    Member member = memberRepository.findByEmail(board.getRegBy());
    boardDTO.setWriterNickname(member.getNickname());



    model.addAttribute("boardDTO", boardDTO);
    model.addAttribute("boardStatus", boardStatus);
    return "board/boardForm";
  }

  @PostMapping({"/{boardStatus}/modify/{boardId}"})
  public String boardUpdate(@PathVariable("boardStatus") BoardStatus boardStatus,
                            @PathVariable("boardId") Long boardId,
                            @Valid BoardDTO boardDTO,
                            BindingResult bindingResult,
                            @RequestParam(value = "boardImgFile", required = false) MultipartFile boardImgFile,
                            Model model) throws  IOException{

    if(bindingResult.hasErrors()){
      return "board/boardForm";
    }

    System.out.println("============= controller1 ===================");
    System.out.println(boardDTO.toString());
    System.out.println("============= controller1 ===================");

  Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
    // 이미지 파일 처리
    if(StringUtils.equalsIgnoreCase(boardStatus, "upgrade") || StringUtils.equalsIgnoreCase(boardStatus, "request")){
      if (boardImgFile != null && !boardImgFile.isEmpty()) {
        try {
          // 파일이 선택된 경우 처리
          BoardDTO boardHaveImg = imgFileService.updateBoardImg(boardId, boardImgFile);
          boardDTO.setBoardOriImgName(boardHaveImg.getBoardOriImgName());
          boardDTO.setBoardImgName(boardHaveImg.getBoardImgName());
          boardDTO.setBoardImgUrl(boardHaveImg.getBoardImgUrl());

          System.out.println("=========== after img file service =========");
          System.out.println(boardDTO.toString());
          System.out.println("=========== after img file service =========");

        }catch (Exception e){
          // 파일이 선택되지 않은 경우 처리
          model.addAttribute("errorMessage", "수정 실패");
          return "board/boardForm";
        }
      }else{
        boardDTO.setBoardOriImgName(board.getBoardOriImgName());
        boardDTO.setBoardImgName(board.getBoardImgName());
        boardDTO.setBoardImgUrl(board.getBoardImgUrl());
      }

    }
//    else if(StringUtils.equalsIgnoreCase(boardStatus, "notice") || StringUtils.equalsIgnoreCase(boardStatus, "question")){
//      boardDTO.setBoardOriImgName(null);
//      boardDTO.setBoardImgName(null);
//      boardDTO.setBoardImgUrl(null);
//    }

    System.out.println("============= controller2 ===================");
    System.out.println(boardDTO.toString());
    System.out.println("============= controller2 ===================");

    boardService.updateBoard(boardDTO);
    return "redirect:/boards/" + boardStatus + "/list";
  }

  @PostMapping({"/{boardStatus}/remove/{boardId}"})
  public String boardDelete(@PathVariable("boardStatus") BoardStatus boardStatus,
                            @PathVariable("boardId") Long boardId) throws Exception {

    imgFileService.deleteBoardImg(boardId);
    boardService.deleteBoard(boardId);
    return "redirect:/boards/" + boardStatus + "/list";
  }

}
