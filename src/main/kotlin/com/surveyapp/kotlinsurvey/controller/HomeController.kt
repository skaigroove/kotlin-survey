package com.surveyapp.kotlinsurvey.controller

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/home") // endpoint
@Controller
class HomeController {
    @GetMapping("/")
    fun home(): String {
        return "home"
    }

    @GetMapping("/logout")
    fun logout(session: HttpSession): String{
        session.invalidate() // 세션 무효화
        return "redirect:/"
    }
}