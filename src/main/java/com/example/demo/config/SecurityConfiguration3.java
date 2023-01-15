package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//필터 초기화와 다중 보안 설정
//admin url 요청이 오면 BasicAuthenticationFilter동작해 자바 스크립트 로그인창 열림
// / url 요청 오면 에러 페이지 나옴, url 과 매칭되는 컨트롤러 안 만들어줘서
//@Order 가 핵심
//httpBasic() 가 @Order(0) 이므로 admin 요청하면 securityFilterChain 에서 securityFilterChain 이 먼저 동작
//만약 securityFilterChain2가 @Order(0) 이면 admin 요청해도 로그인창 안열림
//@Configuration
public class SecurityConfiguration3 {

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/admin/**").authenticated()
                .and()
                //httpBasic() 은 BasicAuthenticationFilter 등록
                .httpBasic();

        return http.build();
    }

    @Bean
    @Order(0)
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                //formLogin() 은 UserNamePasswordAuthenticationFilter 등록
                .formLogin();

        return http.build();
    }
}