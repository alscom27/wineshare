package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.CellarDetailDTO;
import com.keduit.wineshare.dto.CellarWineDTO;

import com.keduit.wineshare.service.CellarService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;



import javax.validation.Valid;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CellarController {

  private final CellarService cellarService;

  // 셀러 목록
  @GetMapping("/cellars")
  public String cellarList() {
    return "cellar/cellarList";
  }

  // 셀러 목록(ajax)
  @GetMapping({"/cellars/list/{page}", "/cellars/list"})
  @ResponseBody
  public ResponseEntity<Map<String, Object>> cellarList(@PathVariable("page") Optional<Integer> page,
                                                        Principal principal) {
    Map<String, Object> response = new HashMap<>();
    Pageable pageable = PageRequest.of(page.orElse(0), 6);
    Page<CellarDetailDTO> cellarWines = cellarService.getCellarWines(pageable, principal.getName());

    response.put("cellarWines", cellarWines.getContent());
    response.put("totalPages", cellarWines.getTotalPages());
    response.put("currentPage", cellarWines.getNumber());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  // 셀러에 와인 추가?
  @PostMapping("/cellars")
  public @ResponseBody ResponseEntity cellar(@RequestBody @Valid CellarWineDTO cellarWineDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
    if(bindingResult.hasErrors()) {
      StringBuilder sb = new StringBuilder();
      List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
      for (FieldError fieldError : fieldErrorList) {
        sb.append(fieldError.getDefaultMessage());
      }
      return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

    if(principal == null) {
      return new ResponseEntity<>("로그인 후 이용하세요.", HttpStatus.UNAUTHORIZED);
    }
    String email = principal.getName();
    Long cellarWineId;

    try {
      cellarWineId = cellarService.addCellar(cellarWineDTO, email);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(cellarWineId, HttpStatus.OK);
  }

  // 셀러에서 와인 삭제
  @DeleteMapping("/cellarWine/{cellarWineId}")
  public @ResponseBody ResponseEntity deleteCellarWine(@PathVariable("cellarWineId") Long cellarWineId,
                                                       Principal principal) {
    if(!cellarService.validationCellarWine(cellarWineId, principal.getName())) {
      return new ResponseEntity<>("삭제 권한이 없습니다.",HttpStatus.BAD_REQUEST);
    }
    cellarService.deleteCellarWine(cellarWineId);
    return new ResponseEntity<>(cellarWineId,HttpStatus.OK);
  }


}
