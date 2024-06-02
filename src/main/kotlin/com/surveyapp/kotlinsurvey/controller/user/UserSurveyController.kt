package com.surveyapp.kotlinsurvey.controller.user

import com.surveyapp.kotlinsurvey.controller.form.AnswerListForm
import com.surveyapp.kotlinsurvey.controller.form.QuestionForm
import com.surveyapp.kotlinsurvey.controller.form.SurveyForm
import com.surveyapp.kotlinsurvey.repository.UserRepository
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

@RequestMapping("/home")
@Controller
class UserSurveyController(
    @Autowired private val surveyService: SurveyService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userService: UserService
) {
    @GetMapping("/list")
    fun viewSurveyList(model: Model, session: HttpSession): String {
        val username = session.getAttribute("username")
        model.addAttribute("username", username)

        val userLoginId = session.getAttribute("loginId") as? String ?: return "redirect:/"

        val user = userRepository.findByLoginId(userLoginId)
        model.addAttribute("user", user)

        val surveyPostList = surveyService.getSurveyList()
        val participatedSurveyIds = surveyService.getParticipatedSurveyIds(userLoginId)
        model.addAttribute("postList", surveyPostList)
        model.addAttribute("participatedSurveyIds", participatedSurveyIds)

        return "user-auth/user-survey/survey-list-user"
    }

    @GetMapping("/post")
    fun createSurveyForm(model: Model, session:HttpSession): String {

        // 모델에 surveyForm 초기화하여 추가
        model.addAttribute("surveyForm", SurveyForm())
        model.addAttribute("questionForm", QuestionForm())

        // 세션에서 사용자 이름 가져와서 헤더애 사용자 이름 정보 추가
        val userLoginId = session.getAttribute("loginId") as? String ?: return "redirect:/"
        val user = userRepository.findByLoginId(userLoginId)
        model.addAttribute("username", user?.name)



        return "user-auth/user-survey/survey-create-user"
    }

    @PostMapping("/post")
    fun createSurvey(
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
            return "redirect:/"

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

    @GetMapping("/list/participate/{surveyId}")
    fun createAnswerForm(@PathVariable surveyId: Long, model: Model, session: HttpSession): String { // 설문 참여

        val survey = surveyService.getSurveyById(surveyId) // surveyId로 설문 정보를 가져옴
        val questions = survey?.questions // 설문 정보로부터 질문 리스트를 가져옴
        val remainingDays = surveyService.getRemainingDays(survey) // 남은 날짜 계산

        val username = session.getAttribute("username") // 세션에서 사용자 이름 가져오기
        model.addAttribute("username", username); // model에 "username" 추가
        model.addAttribute("survey", survey) // 참여 할 설문 정보를 model에 추가
        model.addAttribute("questions", questions) // 질문 리스트를 model에 추가
        model.addAttribute("answerListForm", AnswerListForm()) // 답변 리스트 폼을 model에 추가
        model.addAttribute("remainingDays", remainingDays) // 남은 날짜를 model에 추가


        return "user-auth/user-survey/survey-response-user" // 경로 반환 : survey-response-user.html
    }

    @PostMapping("/list/participate/{surveyId}")
    fun responseSurvey(
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

        val loginId = session.getAttribute("loginId") as? String ?: return "redirect:/"
        val user = userRepository.findByLoginId(loginId) ?: return "redirect:/"
        val survey = surveyService.getSurveyById(surveyId) ?: return "redirect:/home/list"

        surveyService.participateSurvey(survey,loginId, answerListForm) // 설문 참여

        return "redirect:/home/list" // 경로 반환 : survey-list-user.html
    }

}
