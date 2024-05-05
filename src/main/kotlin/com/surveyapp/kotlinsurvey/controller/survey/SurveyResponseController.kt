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
class SurveyResponseController(
    @Autowired private val surveyService: SurveyService,
    @Autowired private val userRepository: UserRepository
) {
    @GetMapping("/list/participate/{surveyId}")
    fun participateSurvey(@PathVariable surveyId: Long, model: Model): String { // 설문 참여
        val survey = surveyService.getSurveyById(surveyId)
        model.addAttribute("participate", survey)

        return "participate" // 경로 반환 : participate.html
    }



    /*
 @PostMapping("/submitSurvey")
 fun submitSurvey(
     @Valid @ModelAttribute("surveyForm") surveyForm: SurveyForm,
     session: HttpSession,
     result: BindingResult,
     redirectAttributes: RedirectAttributes,
 )
  */

}