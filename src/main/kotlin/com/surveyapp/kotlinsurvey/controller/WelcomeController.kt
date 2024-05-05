package com.surveyapp.kotlinsurvey.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping



@Controller
class WelcomeController {
    // 첫 화면으로 보여질 루트 페이지
    @GetMapping("/")
    fun welcomePage(): String {
        return "welcome" // welcome.html 뷰 반환
    }
}