package com.keduit.wineshare.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

  // 사진 파일 저장
  public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{

    // 디렉토리 생성 코드 추가
    File directory = new File(uploadPath);
    if (!directory.exists()) {
      directory.mkdirs(); // 디렉토리가 없으면 생성
    }

    UUID uuid = UUID.randomUUID();
    String extention = originalFileName.substring(originalFileName.lastIndexOf("."));
    String savedFileName = uuid.toString() + extention;
    String fileUploadFullUrl = uploadPath + "/"+savedFileName;

    FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
    fos.write(fileData);
    fos.close();
    return savedFileName;
  }

  // 사진 파일 삭제
  public void deleteFile(String filePath) throws Exception{
    File file = new File(filePath);

    if(file.exists()){
      file.delete();
      log.info("파일 삭제");
    }else{
      log.info("파일 존재 x");
    }
  }

}
