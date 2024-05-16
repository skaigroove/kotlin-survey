package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.controller.form.AnswerListForm
import com.surveyapp.kotlinsurvey.controller.form.SurveyForm
import com.surveyapp.kotlinsurvey.domain.answer.Answer
import com.surveyapp.kotlinsurvey.domain.answer.AnswerType
import com.surveyapp.kotlinsurvey.domain.answer.ChoiceAnswer
import com.surveyapp.kotlinsurvey.domain.answer.TextAnswer
import com.surveyapp.kotlinsurvey.domain.question.Question
import com.surveyapp.kotlinsurvey.domain.question.QuestionOption
import com.surveyapp.kotlinsurvey.domain.question.QuestionType
import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.repository.SurveyParticipationRepository
import com.surveyapp.kotlinsurvey.repository.SurveyRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
@Transactional
class SurveyService(
    @Autowired private val surveyRepository: SurveyRepository,
    @Autowired private val surveyParticipationRepository: SurveyParticipationRepository,
    @Autowired private val userService: UserService,
) {

    fun saveSurvey(survey: Survey) {
        surveyRepository.saveSurvey(survey)
    }

    fun getSurveyById(surveyId: Long): Survey? { return surveyRepository.getSurveyById(surveyId) }

    fun getSurveyList(): List<Survey>? { return surveyRepository.getSurveyList() }

    fun getQuestionList(): List<Question>? { return surveyRepository.getQuestionList() }

    fun createSurvey(surveyForm: SurveyForm, user: User): Survey { // 설문 조사 생성 함수

        // 받아 온 surveyForm 의 값을 Survey 생성자에 넣어 survey 생성
        val survey = Survey(
            null, user, surveyForm.title, surveyForm.description, surveyForm.startDate, surveyForm.endDate
        )

        val questions = createQuestions(surveyForm, survey) // 질문을 surveyForm의 questionForm으로부터 생성하고 Repository에 저장

        // Survey 객체에 질문 리스트 설정
        survey.questions.addAll(questions)

        return survey
    }

    private fun createQuestions(form: SurveyForm, survey: Survey): MutableList<Question> {
        return form.questions.map { questionForm ->
            val question = Question(
                questionId = null,  // 신규 생성이므로 ID는 null
                survey = survey,    // 부모 설문조사 객체 연결
                context = questionForm.context,  // 질문 내용
                questionType = questionForm.questionType  // 질문 유형
            )

            var index = 1

            // 객관식 질문일 경우 선택지를 추가
            if (questionForm.questionType == QuestionType.MULTIPLE_CHOICE) {
                question.questionOptions = questionForm.questionOptions.map { optionForm ->
                    QuestionOption(
                        questionOptionId = null,
                        optionIndex = index++,
                        questionOptionText = optionForm.optionText,  // 선택지 텍스트
                        question = question  // 부모 질문 객체 연결
                    )
                }.toMutableList()
            }
            question
        }.toMutableList()
    }

    fun participateSurvey(survey: Survey, loginId : String, answerListForm: AnswerListForm) : Survey {

        val user = userService.findUserByLoginId(loginId)
        if(user == null)
            throw IllegalArgumentException("participate User not found")

        val surveyParticipation = SurveyParticipation(null,user,survey, LocalDate.now()) // SurveyParticipation 객체 생성

        // survey에 answerForm을 통해 받은 답변을 추가
        for( alf in answerListForm.answerList) { // answerListForm을 통해 받은 답변을 survey에 추가

            val question = survey.findQuestion(alf.questionId) // questionId를 통해 question을 찾음

            val answer : Answer = when(question?.questionType) { // questionType에 따라 다른 Answer 객체 생성
                QuestionType.MULTIPLE_CHOICE -> ChoiceAnswer(null,user,question,surveyParticipation,AnswerType.MULTIPLE_CHOICE,null, findQuestionOptionById(alf.selectedOption!!))
                QuestionType.SUBJECTIVE -> TextAnswer(null,user,question,surveyParticipation,AnswerType.SUBJECTIVE,alf.text)
                else -> { throw IllegalArgumentException("Invalid Question Type")}
            }
            question.answers.add(answer) // 각 question에 answer 추가
        }

        surveyParticipationRepository.saveParticipation(surveyParticipation) // SurveyParticipation 저장
        surveyRepository.mergeSurvey(survey) // mergeSurvey()를 통해 survey 업데이트

        return survey
    }

    fun findQuestionOptionById(questionOptionId: Long): QuestionOption? {
        return surveyRepository.findQuestionOptionById(questionOptionId)
    }

}