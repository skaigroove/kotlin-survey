package com.surveyapp.kotlinsurvey.controller.survey

import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping


@RequestMapping("/home") // endpoint
@Controller
class SurveyListController(
    @Autowired private val surveyService: SurveyService
) {
    @GetMapping("/list")
    fun list(model: Model): String { // 설문 목록
        val surveyPostList = surveyService.getSurveyList() // Survey table 에 기록된 모든 설문 get

        model.addAttribute("postList", surveyPostList) // "postList" 로 surveyPostList 추가

        return "list" // 경로 반환 : list.html
    }
}