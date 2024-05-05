package com.surveyapp.kotlinsurvey.controller.survey

import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@RequestMapping("/home") // endpoint
@Controller
class SurveyResponseController(
    @Autowired private val surveyService: SurveyService,
    @Autowired private val userRepository: UserRepository
) {


}