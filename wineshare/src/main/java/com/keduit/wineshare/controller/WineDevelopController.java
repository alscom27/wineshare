package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.service.WineDevelopService;
import com.keduit.wineshare.service.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@RestController
@RequestMapping("/develops")
@RequiredArgsConstructor
public class WineDevelopController {

  private final WineDevelopService wineDevelopService;
  private final WineService wineService;
  private final MemberRepository memberRepository;

  @GetMapping({"/{wineId}", "/{wineId}/{developPage}"})
  public ResponseEntity<Map<String, Object>> getWineDevelopList(
      @PathVariable("wineId") Long wineId,
      @PathVariable("developPage") Optional<Integer> developPage) {

    Pageable pageable = PageRequest.of(developPage.orElse(0), 3);
    Page<WineDevelopDTO> wineDevelopDTOList = wineDevelopService.getDevelopPageByWine(wineId, pageable);


    Map<String, Object> response = new HashMap<>();
    response.put("wineDevelops", wineDevelopDTOList.getContent());
    response.put("currentPage", wineDevelopDTOList.getNumber());
    response.put("maxPage", 5);
    response.put("totalPages", wineDevelopDTOList.getTotalPages());


    return ResponseEntity.ok(response);
  }
}
