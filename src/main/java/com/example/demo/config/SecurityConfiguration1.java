package com.example.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

//@Configuration
public class SecurityConfiguration1 {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated();

        http
                .formLogin()
//                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("Authentication : " + authentication.getName());
                        response.sendRedirect("/");
                    }
                })

                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("Exception : " + exception.getMessage());
                        response.sendRedirect("/login");
                    }
                })
                .permitAll();

        http
                .logout()
                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID", "remember-me")
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        //세션 삭제
                        HttpSession session = request.getSession();
                        session.invalidate();
                        System.out.println("세션 무효화");
                    }
                })
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("로그아웃");
                        response.sendRedirect("/login");
                    }
                })
                .permitAll();

        http
                .rememberMe()
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(3600)
                .alwaysRemember(false)
                .userDetailsService(userDetailsService);

        http
                .sessionManagement()
//                .invalidSessionUrl("/invalid")  // 세션이 유효하지 않을 때 이동 할 페이지
                .maximumSessions(1)             // 최대 허용 가능 세션 수 , -1 : 무제한 로그인 세션 허용
                .maxSessionsPreventsLogin(true) // 동시 로그인 차단함,  false : 기존 세션 만료(default)
                .expiredUrl("/login");	        // 세션이 만료된 경우 이동 할 페이지, 두 번쨰에 로그인한 사용자 로그인 페이지로 이동됨

//        http
//                .sessionManagement()
//                .sessionFixation().changeSessionId(); // 기본값 none, migrateSession, newSession

        return http.build();
    }
    
}
