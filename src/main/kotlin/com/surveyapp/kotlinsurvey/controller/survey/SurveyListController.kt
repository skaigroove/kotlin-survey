package com.surveyapp.kotlinsurvey.controller.survey

import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/home")
@Controller
class SurveyListController(
    @Autowired private val surveyService: SurveyService,
    @Autowired private val userRepository: UserRepository
) {
    @GetMapping("/list")
    fun list(model: Model, session: HttpSession): String {
        val username = session.getAttribute("username")
        model.addAttribute("username", username)

        val userLoginId = session.getAttribute("loginId") as? String ?: return "redirect:/"
        val user = userRepository.findByLoginId(userLoginId)
        model.addAttribute("user", user)

        val surveyPostList = surveyService.getAllSurveys()
        val participatedSurveyIds = surveyService.findParticipatedSurveysByLoginId(userLoginId)
        model.addAttribute("postList", surveyPostList)
        model.addAttribute("participatedSurveyIds", participatedSurveyIds)

        return "list"
    }
}
