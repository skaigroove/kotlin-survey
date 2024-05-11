package com.surveyapp.kotlinsurvey.controller.survey

import com.surveyapp.kotlinsurvey.controller.form.AnswerListForm
import com.surveyapp.kotlinsurvey.domain.question.QuestionType
import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@RequestMapping("/home") // endpoint
@Controller
class SurveyResponseController(
    @Autowired private val surveyService: SurveyService,
    @Autowired private val userRepository: UserRepository,
) {
    @GetMapping("/list/participate/{surveyId}")
    fun createAnswerForm(@PathVariable surveyId: Long, model: Model): String { // 설문 참여

        val survey = surveyService.getSurveyById(surveyId) // surveyId로 설문 정보를 가져옴
        val questions = survey?.questions // 설문 정보로부터 질문 리스트를 가져옴

        model.addAttribute("survey", survey) // 참여 할 설문 정보를 model에 추가
        model.addAttribute("questions", questions) // 질문 리스트를 model에 추가
        model.addAttribute("answerListForm", AnswerListForm()) // 답변 리스트 폼을 model에 추가

        return "participate" // 경로 반환 : participate.html
    }

    @PostMapping("/list/participate/{surveyId}")
    fun participateSurvey(
        @PathVariable surveyId: Long,
        @Valid @ModelAttribute answerListForm: AnswerListForm,
        session: HttpSession,
        result: BindingResult,
        redirectAttributes: RedirectAttributes,
    ): String {
        if (result.hasErrors()) {
            result.allErrors.forEach { error ->
                println("Error: ${error.defaultMessage}")
            } // binding 에 에러가 있을 경우 에러 매세지 출력
        }

        val loginId = session.getAttribute("loginId") as? String ?: return "redirect:/login"
        val user = userRepository.findByLoginId(loginId) ?: return "redirect:/login"
        val survey = surveyService.getSurveyById(surveyId) ?: return "redirect:/home/list"

        surveyService.participateSurvey(survey,loginId, answerListForm) // 설문 참여

        return "redirect:/home/list" // 경로 반환 : list.html
    }
}