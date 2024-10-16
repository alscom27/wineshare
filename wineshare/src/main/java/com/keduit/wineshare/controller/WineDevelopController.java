package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.repository.WineRepository;
import com.keduit.wineshare.service.WineDevelopService;
import com.keduit.wineshare.service.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/develops")
@RequiredArgsConstructor
public class WineDevelopController {

  private final WineDevelopService wineDevelopService;
  private final WineService wineService;
  private final MemberRepository memberRepository;
  private final WineRepository wineRepository;

  // 평가 목록
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

  // 평가 등록
  @PostMapping("/new/{wineId}")
  public ResponseEntity<String> registerDevelop(@RequestBody WineDevelopDTO wineDevelopDTO,
                                                @PathVariable("wineId") Long wineId,
                                                Principal principal) {
    Member member = memberRepository.findByEmail(principal.getName());
    Wine wine = wineRepository.findById(wineId).orElseThrow(EntityNotFoundException::new);
    wineDevelopDTO.setMemberId(member.getId());
    wineDevelopDTO.setWineId(wine.getId());
    wineDevelopService.saveWineDevelop(wineDevelopDTO);
    return new ResponseEntity<>("평가가 등록되었습니다.", HttpStatus.CREATED);
  }

  // 평가 삭제
  @DeleteMapping("/{developId}")
  public ResponseEntity<String> deleteDevelop(@PathVariable("developId") Long developId,
                                              Principal principal) {

    if(!wineDevelopService.validationWineDevelop(developId, principal.getName())){
      return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    wineDevelopService.remove(developId);
    return new ResponseEntity<>("평가가 삭제되었습니다.", HttpStatus.OK);
  }

  // 평가 수정
  @PutMapping("/{developId}")
  public ResponseEntity<String> modifyDevelop(@PathVariable("developId") Long developId,
                                              @RequestBody WineDevelopDTO wineDevelopDTO,
                                              Principal principal) {
    if(!wineDevelopService.validationWineDevelop(developId, principal.getName())){
      return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
    wineDevelopService.modifyDevelop(developId, wineDevelopDTO);
    return new ResponseEntity<>("평가가 수정되었습니다.", HttpStatus.OK);
  }

}
