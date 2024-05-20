package com.surveyapp.kotlinsurvey.controller

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@RequestMapping("/home") // endpoint
@Controller
class HomeController {
    @GetMapping("/")
    fun home(session: HttpSession, model:Model): String {

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username);

        return "home"
    }

    @GetMapping("/admin")
    fun adminHome(session: HttpSession, model:Model): String {

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username);

        return "home-admin"
    }

    @GetMapping("/logout")
    fun logout(session: HttpSession): String{
        session.invalidate() // 세션 무효화
        return "redirect:/"
    }

    @GetMapping("/session-username")
    @ResponseBody
    fun getSessionUsername(session: HttpSession): Map<String, String> {
        val username = session.getAttribute("username") as? String ?: "defaultUser" // 세션에서 사용자 이름 가져오기
        return mapOf("username" to username)
    }
}