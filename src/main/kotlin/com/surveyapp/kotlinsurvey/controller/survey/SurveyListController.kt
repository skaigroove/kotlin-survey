package com.surveyapp.kotlinsurvey.controller.survey

import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping


@RequestMapping("/home") // endpoint
@Controller
class SurveyListController(
    @Autowired private val surveyService: SurveyService,
) {
    @GetMapping("/list")
    fun list(model: Model, session: HttpSession): String { // 설문 목록
        val username = session.getAttribute("username") // 세션에서 사용자 이름 가져오기
        model.addAttribute("username", username); // model에 "username" 추가

        val surveyPostList = surveyService.getSurveyList() // 모든 설문 리스트 가져오기
        model.addAttribute("postList", surveyPostList) // "postList" 로 surveyPostList 추가

        return "list" // 경로 반환 : list.html
    }
}