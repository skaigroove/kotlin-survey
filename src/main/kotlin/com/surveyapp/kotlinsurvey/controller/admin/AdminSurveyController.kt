package com.surveyapp.kotlinsurvey.controller.admin


import com.surveyapp.kotlinsurvey.repository.SurveyParticipationRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping("/admin")
class AdminSurveyController(
    @Autowired private val userService: UserService,
    @Autowired private val surveyService: SurveyService,
    @Autowired private val surveyParticipationRepository: SurveyParticipationRepository,
    ) {

    @GetMapping("/survey")
    fun adminSurveyList(model: Model, session:HttpSession): String { // 관리자 (admin) - 설문 목록 관련 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val surveyList = surveyService.getSurveyList() // 모든 설문 목록을 list 로 get

        model.addAttribute("adminSurveyList", surveyList) // 모든 설문 목록을 model 속성으로 추가

        return "admin-auth/admin-survey/admin-survey-list" // 경로 반환
    }

    @GetMapping("/survey/{surveyId}")
    fun adminDetailSurvey(@PathVariable surveyId: Long, model: Model, session:HttpSession): String { // 관리자 (admin) - 설문 열람 (상세 보기) 관련 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val survey = surveyService.getSurveyById(surveyId) // surveyId 에 해당하는 설문 get
        val participations = surveyParticipationRepository.getSurveyParticipationListBySurveyId(surveyId)

        model.addAttribute("adminSurveyPost", survey) // 해당 설문을 model 속성으로 추가
        model.addAttribute("adminSurveyParticipations", participations) // 해당 설문을 model 속성으로 추가

        return "admin-auth/admin-survey/admin-survey-detail" // 경로 반환
    }

    @PostMapping("/survey/delete/{surveyId}")
    @ResponseBody
    fun adminSurveyDelete(@PathVariable surveyId: Long, session: HttpSession): ResponseEntity<String> { // 관리자 (admin) - 설문 삭제 관련 처리
        // 로그인 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")

        // 설문 삭제 처리
        surveyService.deleteSurvey(surveyId) // 해당 설문 삭제

        return ResponseEntity.ok("Survey deleted successfully")
    }
}