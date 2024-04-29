package com.surveyapp.kotlinsurvey.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping



@Controller
class LoginController {

    // 첫 화면으로 보여질 루트 페이지
    @GetMapping("/")
    fun RootPage(): String {
        return "index" // login.html 뷰 반환
    }

    @GetMapping("/signup")
    fun SignUpPage(): String {
        return "signup"
    }
    // 로그인 페이지
    @GetMapping("/login")
    fun LoginPage(): String {
        return "login"
    }

    // 로그인 성공 시 리다이렉션 될 페이지
    @GetMapping("/home")
    fun homePage(): String {
        return "home" // home.html 뷰 반환
    }
}