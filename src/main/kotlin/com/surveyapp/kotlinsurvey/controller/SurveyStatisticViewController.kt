package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.repository.SurveyRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/home")
class SurveyStatisticViewController(@Autowired private val surveyRepository: SurveyRepository) {

    @GetMapping("/list/statistic/{surveyId}")
    fun showSurveyStatisticPage(@PathVariable surveyId: Long, model: Model): String {

        val statistics = surveyRepository.getSurveyStatisticsMultipleChoice(surveyId)

            model.addAttribute("surveyId", surveyId)
            model.addAttribute("statistics", statistics)

            return "statistic"  // Thymeleaf 템플릿 이름
    }
}
