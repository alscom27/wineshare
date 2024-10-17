package com.keduit.wineshare.config;

import com.keduit.wineshare.constant.EventOrNot;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventCategoryConverter implements Converter<String, EventOrNot> {
  @Override
  public EventOrNot convert(String source) {
    return EventOrNot.valueOf(source.toUpperCase());
  }
}
