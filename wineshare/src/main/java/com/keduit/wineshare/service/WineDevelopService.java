package com.keduit.wineshare.service;

import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.*;
import com.keduit.wineshare.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class WineDevelopService {

  private final WineDevelopRepository wineDevelopRepository;

  private final WineRepository wineRepository;
  private final MemberRepository memberRepository;
  private final FoodPairingRepository foodPairingRepository;


  // 평가 등록
  public void saveWineDevelop(WineDevelopDTO wineDevelopDTO) {

    // Wine ID로 디벨롭을 등록할 와인을 가져온다
    Wine wine = wineRepository.findById(wineDevelopDTO.getWineId()).orElseThrow(EntityNotFoundException::new);

    // 멤버 가져오기
    Member member = memberRepository.findById(wineDevelopDTO.getMemberId()).orElseThrow(EntityNotFoundException::new);

    // Form 을 통해 등록된 DTO 를 Entity 로 변환
    WineDevelop wineDevelop = new WineDevelop();
    wineDevelop.setMember(member);
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


  // 특정 와인에 속한 모든 와인디벨롭을 리스트로 반환 하는 메소드(목록 -> 지워도되나? 나중에확인)
  public List<WineDevelopDTO> findAllByWine(Wine wine) {

    List<WineDevelop> wineDevelops = wineDevelopRepository.findByWine(wine);
    List<WineDevelopDTO> wineDevelopDTOList = new ArrayList<>();
    for (WineDevelop wineDevelop : wineDevelops) {
      WineDevelopDTO wineDevelopDTO = new WineDevelopDTO();
      wineDevelopDTO.setWineId(wineDevelop.getWine().getId());
      wineDevelopDTO.setMemberId(wineDevelop.getMember().getId());
      wineDevelopDTO.setExpertRating(wineDevelop.getExpertRating());
      wineDevelopDTO.setExpertComment(wineDevelop.getExpertComment());
      wineDevelopDTO.setSweetness(wineDevelop.getSweetness());
      wineDevelopDTO.setAcidity(wineDevelop.getAcidity());
      wineDevelopDTO.setBody(wineDevelop.getBody());
      wineDevelopDTO.setTannin(wineDevelop.getTannin());
      wineDevelopDTO.setFizz(wineDevelop.getFizz());
      wineDevelopDTO.setAromaOne(wineDevelop.getAromaOne());
      wineDevelopDTO.setAromaTwo(wineDevelop.getAromaTwo());
      wineDevelopDTO.setFoodOne(wineDevelop.getFoodOne());
      wineDevelopDTO.setFoodTwo(wineDevelop.getFoodTwo());
      wineDevelopDTOList.add(wineDevelopDTO);
    }

    return wineDevelopDTOList;
  }

  // 목록(ajax)
  @Transactional(readOnly = true)
  public Page<WineDevelopDTO> getDevelopPageByWine(Long wineId, Pageable pageable) {
    return wineDevelopRepository.getDevelopPageByWine(wineId, pageable);
  }


  // 리스트로 받은 와인디벨롭의 각 항목을 평균내거나 카운트하는 메소드
  public WineDevelopDTO getCountDevelop(Wine wine) {
    List<WineDevelop> wineDevelops = wineDevelopRepository.findByWine(wine);
    double expertRating = wineDevelops.stream().mapToDouble(WineDevelop::getExpertRating).average().orElse(0.0);
    double sweetness = wineDevelops.stream().mapToDouble(WineDevelop::getSweetness).average().orElse(0.0);
    double acidity = wineDevelops.stream().mapToDouble(WineDevelop::getAcidity).average().orElse(0.0);
    double body = wineDevelops.stream().mapToDouble(WineDevelop::getBody).average().orElse(0.0);
    double tannin = wineDevelops.stream().mapToDouble(WineDevelop::getTannin).average().orElse(0.0);
    double fizz = wineDevelops.stream().mapToDouble(WineDevelop::getFizz).average().orElse(0.0);

    List<Object[]> aromaOnes = wineDevelopRepository.countAromaOneByWine(wine);
    List<Object[]> aromaTwos = wineDevelopRepository.countAromaTwoByWine(wine);
    List<Object[]> foodOnes = wineDevelopRepository.countFoodOneByWine(wine);
    List<Object[]> foodTwos = wineDevelopRepository.countFoodTwoByWine(wine);


    String mostAromaOne = aromaOnes.isEmpty() ? null : (String)aromaOnes.get(0)[0];
    String mostAromaTwo = aromaTwos.isEmpty() ? null : Objects.equals((String) aromaTwos.get(0)[0], mostAromaOne) ? (String)aromaTwos.get(1)[0] : (String)aromaTwos.get(0)[0];
    String mostFoodOne = foodOnes.isEmpty() ? null : (String)foodOnes.get(0)[0];
    String mostFoodTwo = foodTwos.isEmpty() ? null : Objects.equals((String) foodTwos.get(0)[0], mostFoodOne) ? (String)foodTwos.get(1)[0] : (String)foodTwos.get(0)[0];

    WineDevelopDTO wineDevelopDTO = new WineDevelopDTO();
    wineDevelopDTO.setExpertRating(expertRating);
    wineDevelopDTO.setSweetness(sweetness);
    wineDevelopDTO.setAcidity(acidity);
    wineDevelopDTO.setBody(body);
    wineDevelopDTO.setTannin(tannin);
    wineDevelopDTO.setFizz(fizz);
    wineDevelopDTO.setAromaOne(mostAromaOne);
    wineDevelopDTO.setAromaTwo(mostAromaTwo);
    wineDevelopDTO.setFoodOne(mostFoodOne);
    wineDevelopDTO.setFoodTwo(mostFoodTwo);
    return wineDevelopDTO;
  }

  // 권한확인(작성자==로그인유저)
  @Transactional(readOnly = true)
  public boolean validationWineDevelop(Long developId, String email) {
    Member member = memberRepository.findByEmail(email);
    WineDevelop wineDevelop = wineDevelopRepository.findById(developId).orElseThrow(EntityNotFoundException::new);
    Member savedMember = wineDevelop.getMember();
    if (!StringUtils.equals(member.getEmail(), savedMember.getEmail())) {
      return false;
    }
    return true;
  }

  // 평가 삭제
  public void remove(Long developId){
    wineDevelopRepository.deleteById(developId);
  }


  // 평가 수정
  public void modifyDevelop(Long developId, WineDevelopDTO wineDevelopDTO) {
    WineDevelop wineDevelop = wineDevelopRepository.findById(developId).orElseThrow(EntityNotFoundException::new);

    wineDevelop.setExpertRating(wineDevelopDTO.getExpertRating());
    wineDevelop.setExpertComment(wineDevelopDTO.getExpertComment());
    wineDevelop.setAromaOne(wineDevelopDTO.getAromaOne());
    wineDevelop.setAromaTwo(wineDevelopDTO.getAromaTwo());
    wineDevelop.setFoodOne(wineDevelopDTO.getFoodOne());
    wineDevelop.setFoodTwo(wineDevelopDTO.getFoodTwo());
    wineDevelop.setFizz(wineDevelopDTO.getFizz());
    wineDevelop.setTannin(wineDevelopDTO.getTannin());
    wineDevelop.setBody(wineDevelopDTO.getBody());
    wineDevelop.setSweetness(wineDevelopDTO.getSweetness());
    wineDevelop.setAcidity(wineDevelopDTO.getAcidity());

    wineDevelopRepository.save(wineDevelop);
  }

  // 평가 삭제시 카운트
  public Long countDevelopByWine(Long wineId) {

    return wineDevelopRepository.countByWineId(wineId);
  }





}
