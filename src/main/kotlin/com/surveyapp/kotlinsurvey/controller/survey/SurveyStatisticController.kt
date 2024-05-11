package com.surveyapp.kotlinsurvey.controller.survey

import com.surveyapp.kotlinsurvey.service.SurveyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/home")
class SurveyStatisticController(@Autowired private val surveyService: SurveyService) {

    @GetMapping("api/list/statistic/{surveyId}")
    fun getSurveyStatistic(@PathVariable surveyId: Long): ResponseEntity<Map<String, Long>> {
        try {
            val statistics = surveyService.getSurveyStatistics(surveyId)
            if (statistics.isNotEmpty()) {
                return ResponseEntity.ok(statistics)
            } else {
                return ResponseEntity.noContent().build()
            }
        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }
}