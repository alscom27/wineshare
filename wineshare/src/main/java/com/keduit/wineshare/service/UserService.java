package com.keduit.wineshare.service;

import com.keduit.wineshare.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public Member saveUser(Member user) {
    validateUser(user);
    return userRepository.save(user);
  }

  private void validateUser(Member user) {
    Member findUser = userRepository.findByEmail(user.getEmail());
    if(findUser != null) {
      throw new IllegalStateException("이미 가입된 회원입니다.");
    }
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member user = userRepository.findByEmail(email);

    if(user == null) {
      throw new UsernameNotFoundException(email);
    }

    return null;
  }
}
