package com.keduit.wineshare.service;

import com.keduit.wineshare.dto.BoardDTO;
import com.keduit.wineshare.dto.MarketingDTO;
import com.keduit.wineshare.dto.WineDTO;
import com.keduit.wineshare.entity.Board;
import com.keduit.wineshare.entity.Marketing;
import com.keduit.wineshare.repository.BoardRepository;
import com.keduit.wineshare.repository.MarketingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImgFileService {

  private final FileService fileService;
  private final BoardRepository boardRepository;
  private final BoardService boardService;
  private final MarketingRepository marketingRepository;

  // 경로바꿔야함 현재 프로젝트 내부로 해놨고 각자의 컴퓨터로 빼놔야함
  // 프로퍼티스도 각자의 경로로 설정하고 이그노어 갈기야함

  // 설정한 업로드 경로
  @Value("${licenseImgLocation}")
  private String licenseImgLocation;  // 게시판 사진 경로

  @Value("${requestImgLocation}")
  private String requestImgLocation;

  @Value("${marketImgLocation}")
  private String marketImgLocation; // 마케팅 사진 경로

  @Value("${wineImgLocation}")
  private String wineImgLocation; // 와인 사진 경로

  //사진 저장
  public BoardDTO saveBoardImg(BoardDTO boardDTO, MultipartFile boardImgFile) throws Exception{

    String originalFileName = boardImgFile.getOriginalFilename();
    String imgName = "";
    String imgUrl = "";

    // 파일 업로드
    if (originalFileName != null && !originalFileName.isEmpty()) {
      // 게시판 상태에 따라 이미지 URL 설정
      if (StringUtils.equalsIgnoreCase(boardDTO.getBoardStatus().name(), "upgrade")) {
        imgName = fileService.uploadFile(licenseImgLocation, originalFileName, boardImgFile.getBytes());
        imgUrl = "/images/certify/" + imgName;
      } else if (StringUtils.equalsIgnoreCase(boardDTO.getBoardStatus().name(), "request")) {
        imgName = fileService.uploadFile(requestImgLocation, originalFileName, boardImgFile.getBytes());
        imgUrl = "/images/request/" + imgName;
      }
    }



    if(imgName != null && imgUrl != null){
      boardDTO.setBoardOriImgName(originalFileName);
      boardDTO.setBoardImgName(imgName);
      boardDTO.setBoardImgUrl(imgUrl);
    }

    return boardDTO;

  }

  // 사진 수정
  public BoardDTO updateBoardImg(Long boardId, MultipartFile boardImgFile) throws Exception{

    BoardDTO boardDTO = BoardDTO.of(boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new));
    String originalFileName = boardImgFile.getOriginalFilename();
    String imgUrl = "";
    String boardImgName = "";

    Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

    if (!boardImgFile.isEmpty()) {
      // 기존 이미지 삭제
      if (!StringUtils.isEmpty(board.getBoardImgName())) {
        if (StringUtils.equalsIgnoreCase(board.getBoardStatus().name(), "upgrade")) {
          fileService.deleteFile(licenseImgLocation + "/" + board.getBoardImgName());
        } else if (StringUtils.equalsIgnoreCase(board.getBoardStatus().name(), "request")) {
          fileService.deleteFile(requestImgLocation + "/" + board.getBoardImgName());
        }
      }

      // 새로운 이미지 업로드
      String boardOriImgName = boardImgFile.getOriginalFilename();
      if (StringUtils.equalsIgnoreCase(boardDTO.getBoardStatus().name(), "upgrade")) {
        boardImgName = fileService.uploadFile(licenseImgLocation, boardOriImgName, boardImgFile.getBytes());
      } else if (StringUtils.equalsIgnoreCase(boardDTO.getBoardStatus().name(), "request")) {
        boardImgName = fileService.uploadFile(requestImgLocation, boardOriImgName, boardImgFile.getBytes());
      }

      // 이미지 URL 설정
      imgUrl = StringUtils.equalsIgnoreCase(board.getBoardStatus().name(), "upgrade") ?
          "/images/certify/" + boardImgName :
          "/images/request/" + boardImgName;

      // DTO에 이미지 정보 설정
      boardDTO.setBoardImgName(boardImgName);
      boardDTO.setBoardImgUrl(imgUrl);
      boardDTO.setBoardOriImgName(originalFileName);
    }

    System.out.println("========= img file service ============");
    System.out.println(boardDTO.toString());
    System.out.println("========= img file service ============");

    return boardDTO;
  }

  // 사진 삭제
  public void deleteBoardImg(Long boardId) throws Exception{
    Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

    // 기존 이미지 삭제
    if (!StringUtils.isEmpty(board.getBoardImgName())) {
      if (StringUtils.equalsIgnoreCase(board.getBoardStatus().name(), "upgrade")) {
        fileService.deleteFile(licenseImgLocation + "/" + board.getBoardImgName());
      } else if (StringUtils.equalsIgnoreCase(board.getBoardStatus().name(), "request")) {
        fileService.deleteFile(requestImgLocation + "/" + board.getBoardImgName());
      }
    }
  }

  /////////////////////// 게시판 ///////////////////

///////////////// 마케팅 /////////////////
//사진 저장
  public MarketingDTO saveMarketImg(MarketingDTO marketingDTO, MultipartFile marketImgFile) throws Exception{

    String originalFileName = marketImgFile.getOriginalFilename();
    String imgName = "";
    String imgUrl = "";

  // 파일 업로드
    imgName = fileService.uploadFile(marketImgLocation, originalFileName, marketImgFile.getBytes());
    imgUrl = "/images/market/" + imgName;

    marketingDTO.setMarketImgName(imgName);
    marketingDTO.setMarketImgUrl(imgUrl);
    marketingDTO.setMarketOriImgName(originalFileName);

  return marketingDTO;
}


  // 사진 수정
  public MarketingDTO updateMarketImg(Long marketingId, MultipartFile marketImgFile) throws Exception{

    Marketing marketing = marketingRepository.findById(marketingId).orElseThrow(EntityNotFoundException::new);
    MarketingDTO marketingDTO = MarketingDTO.of(marketing);

    String originalFileName = marketImgFile.getOriginalFilename();
    String imgUrl = "";
    String marketImgName = "";

    if (!marketImgFile.isEmpty()) {
      // 기존 이미지 삭제
      if (!StringUtils.isEmpty(marketing.getMarketImgName())) {
        fileService.deleteFile(marketImgLocation + "/" + marketing.getMarketImgName());
      }

      // 새로운 이미지 업로드
      String marketOriImgName = marketImgFile.getOriginalFilename();
      marketImgName = fileService.uploadFile(marketImgLocation, marketOriImgName, marketImgFile.getBytes());

      // 이미지 URL 설정
      imgUrl = "/images/market/" + marketImgName;

      // DTO에 이미지 정보 설정
      marketingDTO.setMarketImgName(marketImgName);
      marketingDTO.setMarketImgUrl(imgUrl);
      marketingDTO.setMarketOriImgName(originalFileName);
    }

//    System.out.println("========= img file service ============");
//    System.out.println(boardDTO.toString());
//    System.out.println("========= img file service ============");

    return marketingDTO;
  }

  // 사진 삭제
  public void deleteMarketImg(Long marketingId) throws Exception{
    Marketing marketing = marketingRepository.findById(marketingId).orElseThrow(EntityNotFoundException::new);

    // 기존 이미지 삭제
    if (!StringUtils.isEmpty(marketing.getMarketImgName())) {
      fileService.deleteFile(marketImgLocation + "/" + marketing.getMarketImgName());
    }

  }


// 와인 이미지 저장 관련
public String saveWineImg(WineDTO wineDTO, MultipartFile wineImgFile) throws Exception{
  String originalFileName = wineImgFile.getOriginalFilename();
  String imgName = "";
  String imgUrl = "";

  // 파일이름
  imgName = fileService.uploadFile(wineImgLocation, originalFileName, wineImgFile.getBytes());
  imgUrl = "/images/wines/" + imgName;



  return imgUrl; // db에 저장할 경로

}
}


