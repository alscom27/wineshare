package com.keduit.wineshare.controller;

import com.keduit.wineshare.dto.CellarDetailDTO;
import com.keduit.wineshare.dto.CellarWineDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.WineRepository;
import com.keduit.wineshare.service.CellarService;
import com.keduit.wineshare.service.WineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cellar")
@Log4j2
public class CellarController {

  private final CellarService cellarService;

  // 셀러 목록
//  @GetMapping("/cellars")
//  public String cellarList(Principal principal, Model model) {
//    List<CellarDetailDTO> cellarDetailDTOList = cellarService.getCellarList(principal.getName());
//    model.addAttribute("cellarWines", cellarDetailDTOList);
//    return "/cellar/cellarList";
//  }

  // 임시
  @GetMapping("/cellarList")
  public void list(){
    log.info("cellarList");
  }
}

//  // 셀러에 와인 추가?
//  @PostMapping("/cellars")
//  public @ResponseBody ResponseEntity cellar(@RequestBody @Valid CellarWineDTO cellarWineDTO,
//                                             BindingResult bindingResult,
//                                             Principal principal) {
//    if(bindingResult.hasErrors()) {
//      StringBuilder sb = new StringBuilder();
//      List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
//      for (FieldError fieldError : fieldErrorList) {
//        sb.append(fieldError.getDefaultMessage());
//      }
//      return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
//    }
//
//    String email = principal.getName();
//    Long cellarWineId;
//
//    try {
//      cellarWineId = cellarService.addCellar(cellarWineDTO, email);
//    } catch (Exception e) {
//      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//    return new ResponseEntity<>(cellarWineId, HttpStatus.OK);
//  }
//
//  // 셀러에서 와인 삭제
//  @DeleteMapping("/cellarWine/{cellarWineId}")
//  public @ResponseBody ResponseEntity deleteCellarWine(@PathVariable("cellarWineId") Long cellarWineId,
//                                                       Principal principal) {
//    if(!cellarService.validationCellarWine(cellarWineId, principal.getName())) {
//      return new ResponseEntity<>("삭제 권한이 없습니다.",HttpStatus.BAD_REQUEST);
//    }
//    cellarService.deleteCellarWine(cellarWineId);
//    return new ResponseEntity<>(cellarWineId,HttpStatus.OK);
//  }
//
//
//}
