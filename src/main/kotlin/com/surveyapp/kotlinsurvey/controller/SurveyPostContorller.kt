package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.service.SurveyService

import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate


//@RequestMapping("/post") // endpoint
@Controller
class SurveyPostContorller(
        @Autowired private val surveyService: SurveyService,
) {

    @GetMapping("/home") // '/' 경로에 대한 HTTP Get 요청을 처리하는 method - list() 지정
    fun list(model: Model): String { // Controller -> view : data 전달할 떄 사용되는 객체 : Model
        val surveyPostList: List<Survey>? = surveyService.getSurveyList()
        model.addAttribute("postList", surveyPostList)

        return "board/list.html" // 경로 반환 - 해당 페이지 -> 사용자에게 응답으로 전달하여 보여 줌
    }

    @GetMapping("/post") // '/' 경로에 대한 HTTP GEt 요청 처리 method - post() 지정
    fun post(): String { return "board/post.html" } // 경로 반환 - 해당 페이지 -> 사용자에게 응답으로 전달하여 보여 줌

    @PostMapping("/post")
    fun write(@ModelAttribute surveyRequest: SurveyRequest, redirectAttributes: RedirectAttributes): String { // 설문 조사 생성

        val survey = createSurvey(surveyRequest) // 설문 조사 생성
        surveyService.saveSurvey(survey) // 생성한 설문 조사 DB 에 저장

        redirectAttributes.addAttribute("surveyId", survey.survey_id) // 생성한 설문 조사 ID => Redirect 속성에 추가함

        return "redirect:/survey/${survey.survey_id}" // 경로 반환 : 생성한 설문 조사 게시 글
    }

    fun createSurvey(surveyRequest: SurveyRequest): Survey { // 설문 조사 생성 함수

        val new_survey = Survey(user = surveyRequest.user, title = surveyRequest.title, discription = surveyRequest.description, startDate = surveyRequest.startDate, endDate = surveyRequest.endDate)

        // 각 질문에 대한 정보를 SurveyRequest 에서 추출하여 Question 객체 생성 및 연결
        var questionId: Long = 1 // questionOptionId 로 쓸 애
        for (questionRequest in surveyRequest.questions) {
            val question = Question(
                    questionId = questionId,
                    context = questionRequest.context,
                    questionType = questionRequest.type,
                    survey = new_survey
            )

            // 객관식 질문 : 옵션 정보 추가 => QuestionOption 객체 생성 및 연결
            if (questionRequest.type == QuestionType.MULTIPLECHOICE) {
                val option = QuestionOption(
                        id = questionId,
                        question = question,
                        option1 = questionRequest.option1,
                        option2 = questionRequest.option2,
                        option3 = questionRequest.option3,
                        option4 = questionRequest.option4,
                        option5 = questionRequest.option5
                )
                question.question_option = option
            }
            new_survey.questions?.add(question)

            questionId += 1
        }

        return new_survey
    }


}