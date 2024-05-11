package com.surveyapp.kotlinsurvey.controller.survey

import com.surveyapp.kotlinsurvey.controller.form.QuestionForm
import com.surveyapp.kotlinsurvey.controller.form.SurveyForm
import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.service.SurveyService
import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate

@RequestMapping("/home") // endpoint
@Controller
class SurveyPostController(
    @Autowired private val surveyService: SurveyService,
    @Autowired private val userService: UserService,
) {
    @GetMapping("/post")
    fun createForm(model: Model): String {

        // 모델에 surveyForm 초기화하여 추가
        model.addAttribute("surveyForm", SurveyForm())
        model.addAttribute("questionForm", QuestionForm())

        return "surveyForm"
    }

    @PostMapping("/post")
    fun write(
        // 설문 조사 생성
        @Valid @ModelAttribute("surveyForm") surveyForm: SurveyForm,
        session: HttpSession,
        result: BindingResult,
        redirectAttributes: RedirectAttributes,
    ): String {

        // surveyForm, questionForm binding 에서 오류가 있을 경우
        if (result.hasErrors()) {
            result.allErrors.forEach { error ->
                println("Error: ${error.defaultMessage}")
            }
        }

        /**
         * 세션에서 로그인 ID 가져와서 Repository로부터 해당 유저 정보를 받아오고, 그 유저 정보로 설문조사를 생성한다.
         */

        // login 여부 확인
        val user = userService.checkLogin(session)
        if (user == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/user/login"

        // 생성한 설문조사를 저장함
        val survey = surveyService.createSurvey(surveyForm, user)

        // 입력된 question 이 없을 때 오류 처리
        if (surveyForm.questions.isEmpty()) return "redirect:/home/post?questionEmptyError=true"

        // 입력된 응답 기한 날짜 중 종료일이 시작일보다 앞설 때 오류 처리
        if (surveyForm.endDate < surveyForm.startDate) return "redirect:/home/post?dateSettingError1=true"

        // 입력된 응답 기한 날짜 중 시작일이 오늘보다 더 빠를 때 오류 처리
        if (surveyForm.startDate < LocalDate.now()) return "redirect:/home/post?dateSettingError2=true"

        surveyService.saveSurvey(survey)

        return "redirect:/home/list"
    }
}