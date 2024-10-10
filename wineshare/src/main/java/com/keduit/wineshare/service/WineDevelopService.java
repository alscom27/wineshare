package com.keduit.wineshare.service;

import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.AromaWheel;
import com.keduit.wineshare.entity.FoodPairing;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.entity.WineDevelop;
import com.keduit.wineshare.repository.AromaWheelRepository;
import com.keduit.wineshare.repository.FoodPairingRepository;
import com.keduit.wineshare.repository.WineDevelopRepository;
import com.keduit.wineshare.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WineDevelopService {

  private final WineDevelopRepository wineDevelopRepository;
  private final FoodPairingRepository foodPairingRepository;
  private final AromaWheelRepository aromaWheelRepository;
  private final WineRepository wineRepository;


  public void saveWineDevelop(WineDevelopDTO wineDevelopDTO) {
    WineDevelop wineDevelop = new WineDevelop();

    // Wine ID로 디벨롭을 등록할 와인을 가져온다
    Wine wine = wineRepository.findById(wineDevelopDTO.getWindId()).orElseThrow(EntityNotFoundException::new);

    // Form 을 통해 등록된 DTO 를 Entity 로 변환
    wineDevelop.setWine(wine);
    wineDevelop.setExpertRating(wineDevelopDTO.getExpertRating());
    wineDevelop.setExpertComment(wineDevelopDTO.getExpertComment());
    wineDevelop.setSweetness(wineDevelopDTO.getSweetness());
    wineDevelop.setAcidity(wineDevelopDTO.getAcidity());
    wineDevelop.setBody(wineDevelopDTO.getBody());
    wineDevelop.setTannin(wineDevelopDTO.getTannin());
    wineDevelop.setFizz(wineDevelopDTO.getFizz());
    wineDevelop.setAromaOne(wineDevelopDTO.getAromaOne());
    wineDevelop.setAromaTwo(wineDevelopDTO.getAromaTwo());
    wineDevelop.setFoodOne(wineDevelopDTO.getFoodOne());
    wineDevelop.setFoodTwo(wineDevelopDTO.getFoodTwo());

    // WineDevelop 객체 저장
    wineDevelopRepository.save(wineDevelop);


  }


  // 특정 와인에 속한 모든 와인디벨롭을 리스트로 반환 하는 메소드
  public List<WineDevelop> findAllByWine(Wine wine) {
    return wineDevelopRepository.findByWine(wine);
  }
}
