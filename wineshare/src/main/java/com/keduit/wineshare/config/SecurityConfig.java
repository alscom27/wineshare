package com.keduit.wineshare.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) throws Exception{
    System.out.println("================ SecurityFilterChain =================");
    http.formLogin()
        .loginPage("/members/login")  //로그인 페이지
        .defaultSuccessUrl("/") //로그인 성공시
        .usernameParameter("email") //중요 username을 email로 사용하기로함
        .failureHandler(customAuthenticationFailureHandler) //로그인 실패했을 때
        .and()  //그리고
        .logout() //로그아웃하면
        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //매쳐로 url 매핑
        .logoutSuccessUrl("/"); //로그아웃 성공하면 다시 메인

    // permitAll() : 모든 사용자가 인증없이 해당 경로에 접근 가능
    // hasRole("ADMIN") : 관리자의 경우 /admin/로 접근하는 경로를 통과시킴
    // anyRequest().authenticated() : 위의 경우 이외의 페이지는 인증절차가 필요함.
    http.authorizeRequests()
        .mvcMatchers("/", "/members/**", "error", "favicon.ico",
            "/wines/**", "/wine", "/boards/**", "/marketings/**", "/admins/**",
            "/cellars/**", "/cellar/**", "/develops/**", "/reviews/**",
            // 여기부터 css
            "/contact/**",
            "/fonts/**",
            "/icon/**",
            "/image/**",
            "/javascript/**",
            "/rev-slider/**","/rev-slider/assets/**","/rev-slider/css/**","/rev-slider/fonts/**","/rev-slider/js/**",
            "/stylesheets/**","/stylesheets/colors/**","/stylesheets/font/**",
            "/wineshare-css/**", "/wineshare-js/**", "/wineshare-img/**").permitAll()
        .mvcMatchers("/admin/**").hasRole("ADMIN")
        .mvcMatchers("/expert/**").hasRole("EXPERT")
        .anyRequest().authenticated();
    // 권한 수정하기

    // 인증되지 않은 사용자가 리소스에 접근하여 실패했을때 처리하는 핸들러 등록
    http.exceptionHandling()
        .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  // /resources/static 폴더의 하위 파일은 인증에서 제외 시킴
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer(){
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler(){
    return null;
  }

}
