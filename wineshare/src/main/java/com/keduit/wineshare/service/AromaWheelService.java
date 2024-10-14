package com.keduit.wineshare.service;

import com.keduit.wineshare.entity.AromaWheel;
import com.keduit.wineshare.repository.AromaWheelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AromaWheelService {

  private final AromaWheelRepository aromaWheelRepository;

  public List<AromaWheel> getAllAromaWheel(){
    return aromaWheelRepository.findAll();
  }

  public AromaWheel getAromaWheelByAroma(String aroma){
    return aromaWheelRepository.findByAroma(aroma);
  }
}
