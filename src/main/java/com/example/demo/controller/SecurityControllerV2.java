package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
//public class SecurityControllerV2 {
//
//    @GetMapping("/")
//    @ResponseBody
//    public String hello() {
//        return "hello";
//    }
//
//    //SecurityConfiguration2의 authenticationEntryPoint 에서
//    //AuthenticationEntryPoint 인터페이스를 구현한 객체 넣어
//    //인증 실패했을 때 가는 로그인 페이지 요청 url 에 매칭하는 컨트롤러 만들어야함
//    //이래야 스프링 시큐리티에서 제공하는 로그인 페이지 나옴
////    @GetMapping("login")
////    public String login() {
////        return "login";
////    }
//
//    @GetMapping("denied")
//    @ResponseBody
//    public String deny() {
//        return "권한이 없음";
//    }
//
//    //SecurityConfiguration2 권한 적용이 잘 되었는지 테스트
//    @GetMapping("user")
//    @ResponseBody
//    public String user() {
//        return "user";
//    }
//
//    @GetMapping("admin/pay")
//    @ResponseBody
//    public String adminPay() {
//        return "adminPay";
//    }
//
//    @GetMapping("admin/**")
//    @ResponseBody
//    public String admin() {
//        return "admin";
//    }
//}
