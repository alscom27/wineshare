package com.keduit.wineshare.config;

import com.keduit.wineshare.constant.BoardStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BoardStatusConverter implements Converter<String, BoardStatus> {

  @Override
  public BoardStatus convert(String source) {
    return BoardStatus.valueOf(source.toUpperCase()); // 대문자로 변환
  }
}
