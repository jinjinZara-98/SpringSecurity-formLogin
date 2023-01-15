package com.example.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

//설정 클래스
//@Configuration
public class SecurityConfiguration2 {

    //사용자 생성하고 권한 설정하는 메서드
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() throws Exception {
        //비번은 특정한 prefix 붙임, 비번을 암호화할때 특정한 비번 유형 알고리즘 방식을 prefix 형태로 적어줘야함
        //알고리즘은 로그인할 때 저장되었던 비밀번호를 다시 복호화하는데 사용되어 로그인 비밀번호와 대조한다
        //적어주지 않으면 아이디가 null 이라고 오류를 보여줌
        UserDetails user = User.withDefaultPasswordEncoder().username("user").password("1111").roles("USER").build();
        UserDetails sys = User.withDefaultPasswordEncoder().username("sys").password("1111").roles("SYS").build();
        //admin권한을 가진 사용자가  user sys권한을 갖고싶다면 .roles("ADMIN", "USER", "SYS") 써줘서 접근 가능하게 할 수 있음
        UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("1111").roles("ADMIN", "SYS", "USER").build();

        return new InMemoryUserDetailsManager(user, sys, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .requestMatchers("/login").permitAll()
                .requestMatchers("/user").hasRole("USER")
                .requestMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
                .requestMatchers("/admin/pay").hasRole("ADMIN")
                .anyRequest().authenticated();
        http
                .formLogin();

        //로그인 성공하게 되면 원래 가고자했떤 url로 이동하는
        http
                .formLogin()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        RequestCache requestCache = new HttpSessionRequestCache();
                        //이 객체를 세션안에 담음
                        SavedRequest savedRequest = requestCache.getRequest(request, response);
                        String redirectUrl= savedRequest.getRedirectUrl();
                        response.sendRedirect(redirectUrl);
                    }
                });

        http
                .exceptionHandling() //예외처리 기능이 작동, ExceptionTranslationFilter 동작
//                //인증 실패시 처리
//                .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                    @Override
//                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//                        //AuthenticationEntryPoint 구현체를 넣어줬으므로 개발자가 직접 생성한 로그인 페이지로 이동한다
//                        //스프링 시큐리티에서 제공하는 로그인 페이지로 이동하려면 이 요청 url 과 매칭되는 컨트롤러를 생성해야합니다.
////                        response.sendRedirect("/login");
//                    }
//                })
                //인가 실패시 처리, 권한밖에 접근 할시
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.sendRedirect("/denied");
                    }
                });

        return http.build();
    }
}