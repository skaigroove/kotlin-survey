package com.surveyapp.kotlinsurvey.controller.user

import com.surveyapp.kotlinsurvey.repository.SurveyRepository
import com.surveyapp.kotlinsurvey.repository.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/home")
class UserStatisticController(@Autowired private val surveyRepository: SurveyRepository, @Autowired private val userRepository: UserRepository)
{
    @GetMapping("/list/statistic/{surveyId}")
    fun showSurveyStatisticPage(@PathVariable surveyId: Long, model: Model, session: HttpSession): String {

        model.addAttribute("surveyId", surveyId)
        model.addAttribute(
            "survey",
            surveyRepository.getSurveyById(surveyId) ?: throw RuntimeException("Survey not found")
        )

        // 세션에서 사용자 이름 가져와서 헤더애 사용자 이름 정보 추가
        val userLoginId = session.getAttribute("loginId") as? String ?: return "redirect:/"
        val user = userRepository.findByLoginId(userLoginId)
        model.addAttribute("username", user?.name)

        return "user-auth/user-survey/survey-statistic-user"
    }
}
