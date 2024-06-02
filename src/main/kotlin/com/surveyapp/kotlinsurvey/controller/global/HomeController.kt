package com.surveyapp.kotlinsurvey.controller.global

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RequestMapping("/home") // endpoint
@Controller
class HomeController() {
    @GetMapping("/")
    fun home(session: HttpSession, model:Model): String { // 고객 (Client) Home

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username);

        return "user-auth/home-user"
    }

    @GetMapping("/logout")
    fun logout(session: HttpSession): String{ // 로그아웃
        session.invalidate() // 세션 무효화
        return "redirect:/"
    }

    @GetMapping("/session-username")
    @ResponseBody
    fun getSessionUsername(session: HttpSession): Map<String, String> { // 사용자 이름 get
        val username = session.getAttribute("username") as? String ?: "defaultUser" // 세션에서 사용자 이름 가져오기
        return mapOf("username" to username)
    }

    @GetMapping("/admin")
    fun adminHome(session: HttpSession, model:Model): String { // 관리자 (Admin) Home

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username);

        return "admin-auth/home-admin"
    }

}