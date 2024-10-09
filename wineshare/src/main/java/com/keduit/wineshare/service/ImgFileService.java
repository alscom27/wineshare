package com.keduit.wineshare.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImgFileService {

  @Value("${boardImgLocation}")
  private String boardImgLocation;  // 설정한 업로드 경로

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

}
