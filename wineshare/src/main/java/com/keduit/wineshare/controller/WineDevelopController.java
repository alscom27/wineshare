package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.WineDevelopDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.entity.WineDevelop;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.repository.WineDevelopRepository;
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
  private final WineDevelopRepository wineDevelopRepository;


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
    try {
      Member member = memberRepository.findByEmail(principal.getName());
      Wine wine = wineRepository.findById(wineId).orElseThrow(() -> new EntityNotFoundException("와인을 찾을 수 없습니다."));

      wineDevelopDTO.setMemberId(member.getId());
      wineDevelopDTO.setWineId(wine.getId());
      wineDevelopService.saveWineDevelop(wineDevelopDTO);

      return ResponseEntity.status(HttpStatus.CREATED).body("평가가 등록되었습니다.");
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 중 오류가 발생했습니다: " + ex.getMessage());
    }
  }

  // 평가 삭제
  @DeleteMapping("/{developId}")
  public ResponseEntity<String> deleteDevelop(@PathVariable("developId") Long developId,
                                              Principal principal) {

    if(!wineDevelopService.validationWineDevelop(developId, principal.getName())){
      return new ResponseEntity<>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    // 삭제 전에 남아있는 평가 개수 확인
    WineDevelop wineDevelop = wineDevelopRepository.findWineDevelopById(developId);
    Long remainingCount = wineDevelopService.countDevelopByWine(wineDevelop.getWine().getId()); // wineId는 해당 평가와 연결된 와인의 ID

    // 최소 하나의 평가가 남아 있어야 함
    if (remainingCount <= 1) {
      return new ResponseEntity<>("최소 하나의 평가를 남겨야 합니다.", HttpStatus.BAD_REQUEST);
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
