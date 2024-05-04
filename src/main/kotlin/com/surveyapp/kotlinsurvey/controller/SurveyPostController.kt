package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.config.QuestionTypeEditor
import com.surveyapp.kotlinsurvey.controller.form.SurveyForm
import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@RequestMapping("/home") // endpoint
@Controller
class SurveyPostController(
    @Autowired private val surveyService: SurveyService,
    @Autowired private val userRepository: UserRepository,
) {
    @GetMapping("/")
    fun home(): String {
        return "home"
    }

    @GetMapping("/post")
    fun createSurveyForm(model: Model): String {

        // 모델에 surveyForm 초기화하여 추가
        model.addAttribute(
            "surveyForm",
            SurveyForm()
        )

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

        // SurveyForm에서 오류가 있을 경우
        if (result.hasErrors()) {
            result.allErrors.forEach { error ->
                println("Error: ${error.defaultMessage}")
            }
        }

        /**
         * 세션에서 로그인 ID 가져와서 Repository로부터 해당 유저 정보를 받아오고, 그 유저 정보로 설문조사를 생성한다.
         */

        // 로그인이 안 되어 있을경우 로그인 페이지로 리다이렉션
        val loginId = session.getAttribute("loginId") as? String
        if (loginId == null) {
            println("사용자 로그인 정보가 없습니다.")
            return "redirect:/user/login"
        }

        // 로그인 유저 정보가 없을 경우 로그인 페이지로 리다이렉션
        val user: User? = userRepository.findByLoginId(loginId)
        if (user == null) {
            return "redirect:/user/login"
        }

        // 생성한 설문조사를 저장함
        val survey = createSurvey(surveyForm, user)
        surveyService.saveSurvey(survey)

        return "home"
    }

    fun createSurvey(surveyForm: SurveyForm, user: User): Survey { // 설문 조사 생성 함수

        // 받아온 surveyForm의 값들을 Survey 생성자에 넣어 survey 생성
        val survey = Survey(
            null,
            user,
            surveyForm.title,
            surveyForm.description,
            surveyForm.startDate,
            surveyForm.endDate
        )

        // QuestionForm 리스트를 Question 리스트로 변환
        val questions = surveyForm.questions.map { form ->
            Question(
                null,
                survey,
                form.context,
                form.questionType,
                if (form.questionType == QuestionType.MULTIPLE_CHOICE) form.options.toMutableList() else mutableListOf()
            )
        }.toMutableList()

        surveyForm.questions.forEach {
            println("Question type: ${it.questionType}, Question context: ${it.context}")
        }

        // Survey 객체에 질문 리스트 설정
        survey.questions.clear()
        survey.questions.addAll(questions)

        return survey
    }

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(QuestionType::class.java, QuestionTypeEditor())
    }
}