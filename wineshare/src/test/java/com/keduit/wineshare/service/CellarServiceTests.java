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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 각 테스트 후 롤백
public class CellarServiceTests {

  @Autowired
  private CellarService cellarService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private WineRepository wineRepository;

  @Autowired
  private CellarRepository cellarRepository;

  @Autowired
  private CellarWineRepository cellarWineRepository;

  private Member member;
  private Wine wine1;
  private Wine wine2;
  private Cellar cellar;

  @BeforeEach
  public void setUp() {
    // 테스트를 위한 데이터 준비
    member = new Member();
    member.setEmail("test@example.com");
    memberRepository.save(member);

    wine1 = new Wine();
    wine1.setWineName("Test Wine 1");
    wine1.setPrice(10000);
    wineRepository.save(wine1);

    wine2 = new Wine();
    wine2.setWineName("Test Wine 2");
    wine2.setPrice(15000);
    wineRepository.save(wine2);

    cellar = Cellar.createCellar(member);
    cellarRepository.save(cellar);
    System.out.println(cellar);

    // 셀러에 와인 추가(삭제 테스트 시 필요)
//    CellarWine cellarWine1 = new CellarWine();
//    cellarWine1.setCellar(cellar);
//    cellarWine1.setWine(wine1);
//    cellarWineRepository.save(cellarWine1);
//
//    CellarWine cellarWine2 = new CellarWine();
//    cellarWine2.setCellar(cellar);
//    cellarWine2.setWine(wine2);
//    cellarWineRepository.save(cellarWine2);
  }

  @Test
  public void testAddMultipleWinesToCellar_OneByOne() {
    // 와인을 하나씩 추가하는 테스트
    CellarWineDTO cellarWineDTO1 = new CellarWineDTO();
    cellarWineDTO1.setWindId(wine1.getId());
    cellarService.addCellar(cellarWineDTO1, member.getEmail());

    CellarWineDTO cellarWineDTO2 = new CellarWineDTO();
    cellarWineDTO2.setWindId(wine2.getId());
    cellarService.addCellar(cellarWineDTO2, member.getEmail());

    // 추가된 와인이 셀러에 있는지 확인
    assertTrue(cellarWineRepository.findByCellarIdAndWineId(cellar.getId(), wine1.getId()) != null);
    assertTrue(cellarWineRepository.findByCellarIdAndWineId(cellar.getId(), wine2.getId()) != null);
  }

  @Test
  public void testGetCellarList_WithMultipleAdditions() {
    // 여러 번 와인을 추가한 후, 셀러 리스트 확인
    CellarWineDTO cellarWineDTO1 = new CellarWineDTO();
    cellarWineDTO1.setWindId(wine1.getId());
    cellarService.addCellar(cellarWineDTO1, member.getEmail());

    CellarWineDTO cellarWineDTO2 = new CellarWineDTO();
    cellarWineDTO2.setWindId(wine2.getId());
    cellarService.addCellar(cellarWineDTO2, member.getEmail());

    // 셀러에 담긴 모든 와인 조회
    List<CellarDetailDTO> cellarList = cellarService.getCellarList(member.getEmail());

    assertFalse(cellarList.isEmpty());
    assertEquals(2, cellarList.size()); // 두 개의 와인이 추가되었으므로
    assertEquals("Test Wine 2", cellarList.get(0).getWineName()); // desc 이므로 나중에 넣은 2번이 리스트의 첫번째로 온다
    assertEquals("Test Wine 1", cellarList.get(1).getWineName());
  }

  @Test
  public void testDeleteCellarWine() {

      // 셀러에 담긴 와인 삭제 전 확인
      CellarWine cellarWineToDelete = cellarWineRepository.findByCellarIdAndWineId(cellar.getId(), wine1.getId());
//      assertNotNull(cellarWineToDelete);  // 와인이 존재하는지 확인

      // wine1 삭제
      cellarService.deleteCellarWine(cellarWineToDelete.getId());

      // 삭제 후 확인
      assertNull(cellarWineRepository.findByCellarIdAndWineId(cellar.getId(), wine1.getId())); // 와인이 더 이상 존재하지 않아야 함
      assertNotNull(cellarWineRepository.findByCellarIdAndWineId(cellar.getId(), wine2.getId())); // wine2는 여전히 존재해야 함


  }
}
