package com.keduit.wineshare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keduit.wineshare.dto.CellarWineDTO;
import com.keduit.wineshare.entity.Member;
import com.keduit.wineshare.entity.Wine;
import com.keduit.wineshare.repository.CellarWineRepository;
import com.keduit.wineshare.repository.MemberRepository;
import com.keduit.wineshare.repository.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class CellarControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private WineRepository wineRepository;

  @Autowired
  private CellarWineRepository cellarWineRepository;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();
    // 데이터 초기화 로직
    // 예를 들어, 테스트 사용자 및 와인 데이터 추가
    Member member = new Member();
    member.setEmail("test@example.com");
    memberRepository.save(member);

    Wine wine1 = new Wine();
    wine1.setWineName("Test Wine 1");
    wine1.setPrice(10000);
    wineRepository.save(wine1);

    // 필요에 따라 추가 와인 데이터 삽입
  }

  // 뷰가 없어서 테스트 불가~ 목은 사용하고 싶지 안흥ㅁ...
  @Test
  @WithMockUser(username = "test@example.com")
  void testCellarList() throws Exception {
    mockMvc.perform(get("/cellar"))
        .andExpect(status().isOk())
        .andExpect(view().name("/cellar/cellarList"))
        .andExpect(model().attributeExists("cellarWines"));
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void testAddCellar() throws Exception {
    CellarWineDTO cellarWineDTO = new CellarWineDTO();
    cellarWineDTO.setWindId(1L);  // 적절한 와인 ID로 설정합니다.

    mockMvc.perform(post("/cellar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cellarWineDTO)))
        .andExpect(status().isOk())
        .andExpect(content().string("1"));  // 기대하는 ID 반환
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void testAddCellarValidationError() throws Exception {
    CellarWineDTO cellarWineDTO = new CellarWineDTO(); // 유효하지 않은 DTO

    mockMvc.perform(post("/cellar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cellarWineDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void testDeleteCellarWine() throws Exception {
    Long cellarWineId = 1L; // 삭제할 셀러 와인 ID

    // 실제 데이터베이스에 해당 ID의 데이터가 있다고 가정합니다.
    mockMvc.perform(delete("/cellarWine/{cellarWineId}", cellarWineId))
        .andExpect(status().isOk())
        .andExpect(content().string("1"));  // 기대하는 ID 반환
  }

  @Test
  @WithMockUser(username = "test@example.com")
  void testDeleteCellarWineUnauthorized() throws Exception {
    Long cellarWineId = 1L; // 삭제할 셀러 와인 ID

    // 삭제 권한이 없는 경우
    mockMvc.perform(delete("/cellarWine/{cellarWineId}", cellarWineId))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("삭제 권한이 없습니다."));
  }
}
