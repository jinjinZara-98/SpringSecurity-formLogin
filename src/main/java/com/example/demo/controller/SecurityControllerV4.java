package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityControllerV4 {

    @GetMapping("/")
    public String index(HttpSession session){
        //SecurityContextHolder 에서 인증 객체 꺼내오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("SecurityContextHolder 에서 꺼낸 인증 객체 = " + authentication + "\n");

        //세션에서 인증 객체 꺼내 오기
        SecurityContext context = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication1 = context.getAuthentication();
        System.out.println("세션 에서 꺼낸 인증 객체 = " + authentication1 + "\n");

        return "home";
    }

    @GetMapping("/thread")
    public String thread(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Authentication authentication =
                                SecurityContextHolder.getContext().getAuthentication();
                        System.out.println("자식 스레드에서 꺼낸 인증 객체 = " + authentication);
                    }
                }
        ).start();
        return "thread";
    }
}
