package com.keduit.wineshare.service;

import com.keduit.wineshare.dto.CellarDetailDTO;
import com.keduit.wineshare.dto.CellarWineDTO;
import com.keduit.wineshare.entity.Cellar;
import com.keduit.wineshare.entity.CellarWine;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.CellarRepository;
import com.keduit.wineshare.repository.CellarWineRepository;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CellarService {

  private final CellarRepository cellarRepository;
  private final MemberRepository memberRepository;
  private final WineRepository wineRepository;
  private final CellarWineRepository cellarWineRepository;


  // 셀러에 추가하기
  public Long addCellar(CellarWineDTO cellarWineDTO, String email) {
    Wine wine = wineRepository.findById(cellarWineDTO.getWineId())
        .orElseThrow(() -> new EntityNotFoundException("와인을 찾을 수 없습니다."));

    if(email == null) {
      throw new EntityNotFoundException("로그인 후 이용하세요.");
    }

    Member member = memberRepository.findByEmail(email); // 이메일이니 아마도 프린시팔

    // 현재 로그인한 회원의 셀러 조회
    Cellar cellar = cellarRepository.findByMemberId(member.getId());
      // 셀러 없으면 셀러 만들어주기
    if(cellar == null) {
      cellar = Cellar.createCellar(member);
      cellarRepository.save(cellar);
    }

    // 추가하려는 와인이 셀러에 있는지 확인
    CellarWine savedCellarWine = cellarWineRepository.findByCellarIdAndWineId(cellar.getId(), wine.getId());

    if(savedCellarWine != null) { // 있으면 경고메시지(나중에 잡아서 띄워주도록)
      throw new IllegalArgumentException("이 와인은 이미 셀러에 존재합니다.");
    } else { // 없으면 추가
      CellarWine cellarWine = CellarWine.createCellarWine(cellar, wine);
      cellarWineRepository.save(cellarWine);
      return cellarWine.getId();
    }
  }

  // 셀러에 담긴 모든 와인을 DTO 리스트로 반환(뷰로 보여주기 위해)
  @Transactional(readOnly = true)
  public List<CellarDetailDTO> getCellarList(String email) {
    List<CellarDetailDTO> cellarDetailDTOList = new ArrayList<>();
    Member member = memberRepository.findByEmail(email);
    Cellar cellar = cellarRepository.findByMemberId(member.getId());
    if (cellar == null) {
      return cellarDetailDTOList;
    }
    cellarDetailDTOList = cellarWineRepository.findCellarDetailDTOList(cellar.getId());

    return cellarDetailDTOList;
  }

  // 로그인한 사람과 셀러 아이디가 같은지 확인
  @Transactional(readOnly = true)
  public boolean validationCellarWine(Long cellarWineId, String email) {
    Member member = memberRepository.findByEmail(email);
    CellarWine cellarWine = cellarWineRepository.findById(cellarWineId).orElseThrow(EntityNotFoundException::new);
    Member savedMember = cellarWine.getCellar().getMember();

    if(!StringUtils.equals(member.getEmail(), savedMember.getEmail())) {
      return false;
    }
    return true;
  }

  public boolean isWineInCellar(Long wineId, String email) {
    // 사용자의 이메일로 Member 객체 조회
    if (email == null) {
      return false;
    }

    Member member = memberRepository.findByEmail(email);
    if (member == null) {
      return false; // 사용자가 존재하지 않으면 false 반환
    }

    // 사용자의 셀러를 조회
    Cellar cellar = cellarRepository.findByMemberId(member.getId());
    if (cellar == null) {
      return false; // 셀러가 없으면 false 반환
    }

    // 해당 셀러에서 와인을 찾음
    CellarWine cellarWine = cellarWineRepository.findByCellarIdAndWineId(cellar.getId(), wineId);
    return cellarWine != null; // 와인이 존재하면 true, 존재하지 않으면 false 반환
  }

  // 셀러에 있는 와인 삭제하기
  public void deleteCellarWine(Long cellarWineId) {
    CellarWine cellarwine = cellarWineRepository.findById(cellarWineId).orElseThrow(EntityNotFoundException::new);
    cellarWineRepository.delete(cellarwine);
  }




}
