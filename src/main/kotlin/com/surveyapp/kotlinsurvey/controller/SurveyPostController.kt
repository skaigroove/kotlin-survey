package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.SurveyService
import jakarta.servlet.http.HttpSession

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@RequestMapping("/home") // endpoint
@Controller
class SurveyPostController(
        @Autowired private val surveyService: SurveyService,
        @Autowired private val userRepository: UserRepository
) {
    @GetMapping("/list") // '/' 경로에 대한 HTTP Get 요청을 처리하는 method - list() 지정
    fun list(model: Model): String { // Controller -> view : data 전달할 떄 사용되는 객체 : Model
        val surveyPostList: List<Survey>? = surveyService.getSurveyList()
        model.addAttribute("postList", surveyPostList)
        return "home.html" // 경로 반환 - 해당 페이지 -> 사용자에게 응답으로 전달하여 보여 줌
    }

    @GetMapping("/post") // '/' 경로에 대한 HTTP GEt 요청 처리 method - post() 지정
    fun post(): String { return "post.html" } // 경로 반환 - 해당 페이지 -> 사용자에게 응답으로 전달하여 보여 줌

    /*
    @PostMapping("/post")
    fun write(@ModelAttribute surveyRequest: SurveyRequest, session: HttpSession, redirectAttributes: RedirectAttributes): String { // 설문 조사 생성
        // 세션에서 로그인 ID 가져오기
        val loginId = session.getAttribute("loginId") as? String
        if (loginId == null) {
            println("사용자 로그인 정보가 없습니다.")
            return "redirect:/user/login" // 로그인 페이지로 리다이렉션
        }

        val user : User? = userRepository.findByLoginId(loginId)

        if(user != null) {
            val survey = createSurvey(surveyRequest, user) // 설문 조사 생성
            surveyService.saveSurvey(survey) // 생성한 설문 조사 DB 에 저장

            redirectAttributes.addAttribute("surveyId", survey.surveyId) // 생성한 설문 조사 ID => Redirect 속성에 추가함

           // return "redirect:/home/board/${survey.surveyId}" // 경로 반환 : 생성한 설문 조사 게시 글
            return "/home/list"
        }
        else
            return "redirect:/user/login"
    }

    fun createSurvey(surveyRequest: SurveyRequest, user : User): Survey { // 설문 조사 생성 함수

        val newSurvey = Survey(user = user, title = surveyRequest.title, discription = surveyRequest.description, startDate = surveyRequest.startDate, endDate = surveyRequest.endDate)

        // 각 질문에 대한 정보를 SurveyRequest 에서 추출하여 Question 객체 생성 및 연결
        for (questionRequest in surveyRequest.questions) {
            val question = Question(
                    context = questionRequest.context,
                    questionType = questionRequest.type,
                    survey = newSurvey
            )

            // 객관식 질문 : 옵션 정보 추가 => QuestionOption 객체 생성 및 연결
            if (questionRequest.type == QuestionType.MULTIPLECHOICE) {
                val option = QuestionOption(
                        id = question.questionId,
                        question = question,
                        option1 = questionRequest.option1,
                        option2 = questionRequest.option2,
                        option3 = questionRequest.option3,
                        option4 = questionRequest.option4,
                        option5 = questionRequest.option5
                )
                question.questionOption = option
            }
            newSurvey.questions?.add(question)
        }
        return newSurvey
    }
     */

    @PostMapping("/post")
    fun write(@RequestBody surveyRequest: SurveyRequest): String {
        val questions = surveyRequest.questions.map { questionRequest ->
            val question = Question(
                    context = questionRequest.context,
                    questionType = questionRequest.type
            )

            println("if 시작")
            if (questionRequest.type == QuestionType.MULTIPLECHOICE) {
                question.questionOption = QuestionOption(
                        id = question.questionId,
                        question = question,
                        option1 = questionRequest.option1,
                        option2 = questionRequest.option2,
                        option3 = questionRequest.option3,
                        option4 = questionRequest.option4,
                        option5 = questionRequest.option5
                )
            }
            question
        }.toMutableList()

        val survey = Survey(
                user = surveyRequest.user,
                title = surveyRequest.title,
                discription = surveyRequest.description,
                startDate = surveyRequest.startDate,
                endDate = surveyRequest.endDate,
                //questions = surveyRequest.questions
        )

        questions.forEach { it.survey = survey }

        surveyService.saveSurvey(survey)

        println("return 전")

        return "redirect:/home/list"
    }

}