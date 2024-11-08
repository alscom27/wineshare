package com.keduit.wineshare.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keduit.wineshare.constant.MemberType;
import com.keduit.wineshare.constant.WithdrawStatus;
import com.keduit.wineshare.dto.MemberDTO;
import com.keduit.wineshare.dto.MemberModifyDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(unique = true)
  private String email;

  private String name;

  @Column(unique = true)
  private String nickname;

  private String password;

  private String phoneNumber;

  private String RRN;    // 주민번호 근데 이건 암호화 복호화 추가예정

  @Enumerated(EnumType.STRING)
  private MemberType memberType;

  @Enumerated(EnumType.STRING)
  private WithdrawStatus withdrawStatus;

  // WineDevelop과의 관계 설정
  @JsonIgnore
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<WineDevelop> wineDevelops; // WineDevelop 리스트

  public static Member createMember(MemberDTO memberDTO, PasswordEncoder passwordEncoder){
    // 유효성 검사 (예: null 체크)
    if (memberDTO == null || passwordEncoder == null) {
      throw new IllegalArgumentException("MemberDTO or PasswordEncoder must not be null.");
    }
    Member member = new Member();
    member.setEmail(memberDTO.getEmail());
    member.setName(memberDTO.getName());
    member.setNickname(memberDTO.getNickname());
    String password = passwordEncoder.encode(memberDTO.getPassword());
    member.setPassword(password);
    member.setPhoneNumber(memberDTO.getPhoneNumber());
    member.setRRN(memberDTO.getRRN());

    member.setMemberType(memberDTO.getMemberType() == MemberType.ADMIN ? MemberType.ADMIN : MemberType.REGULAR);

    member.setWithdrawStatus(WithdrawStatus.STAY);
    return member;

  }

  public void updateMember(MemberModifyDTO memberModifyDTO){
    this.setName(memberModifyDTO.getName());
    this.setNickname(memberModifyDTO.getNickname());
    this.setPhoneNumber(memberModifyDTO.getPhoneNumber());
  }

  public void upgradeMember(Member member){
    this.setMemberType(member.getMemberType());
  }

}
