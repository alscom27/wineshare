package com.keduit.wineshare.config;

import com.keduit.wineshare.constant.MarketCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MarketingCategoryConverter implements Converter<String, MarketCategory> {
  @Override
  public MarketCategory convert(String source) {
    return MarketCategory.valueOf(source.toUpperCase());
  }
}
