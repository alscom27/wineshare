package com.keduit.wineshare.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImgFileService {
  // 경로바꿔야함 현재 프로젝트 내부로 해놨고 각자의 컴퓨터로 빼놔야함
  // 프로퍼티스도 각자의 경로로 설정하고 이그노어 갈기야함

  // 설정한 업로드 경로
  @Value("${boardImgLocation}")
  private String boardImgLocation;  // 게시판 사진 경로

  @Value("{marketingImgLocation")
  private String marketingImgLocation; // 마케팅 사진 경로

  public String saveBoardImg(MultipartFile boardImgFile) throws IOException{
    // uuid생성
    String uuid = UUID.randomUUID().toString();

    // 파일 확장자 가져오기
    String originalFileName = boardImgFile.getOriginalFilename();
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

    // 새로운 파일 이름
    String fileName = uuid + extension;

    // 파일 저장 경로
    File directory = new File(boardImgLocation);
    if(!directory.exists()){
      directory.mkdirs(); // 디렉토리가 없으면 생성
    }

    File saveFile = new File(directory, fileName);
    boardImgFile.transferTo(saveFile); // 파일 저장

    // 저장된 파일 경로 반환 (DB에 저장할 경로)
    return fileName; //uuid 파일 이름 반환

  }

  // 위에 save보드이미지를 마케팅이미지로 바꾸고 경로 틀기
  public String saveMarketingImg(MultipartFile marketingImgFile) throws IOException{
    String uuid = UUID.randomUUID().toString();

    // 파일 확장자 가져오기
    String originalFileName =marketingImgFile.getOriginalFilename();
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

    // 새로운 파일 이름
    String fileName = uuid + extension;

    // 파일 저장 경로
    File directory = new File(marketingImgLocation);
    if(!directory.exists()){
      directory.mkdirs(); // 디렉토리가 없으면 생성
    }

    File saveFile = new File(directory, fileName);
    marketingImgFile.transferTo(saveFile); // 파일 저장

    // 저장된 파일 경로 반환 (DB에 저장할 경로)
    return fileName; //uuid 파일 이름 반환

  }

}
