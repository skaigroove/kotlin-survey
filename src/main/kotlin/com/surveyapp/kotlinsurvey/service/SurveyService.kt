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
import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
    private val questionRepository: QuestionRepository,
    private val questionOptionRepository: QuestionOptionRepository,
    private val surveyParticipationRepository: SurveyParticipationRepository,
    private val userRepository: UserRepository,
    private val userService: UserService
) {

    @Transactional
    fun saveSurvey(survey: Survey) {
        surveyRepository.save(survey)
    }

    fun getSurveyById(id: Long): Survey? {
        return surveyRepository.findById(id).orElse(null)
    }

    fun getAllSurveys(): List<Survey>? {
        return surveyRepository.findAllSurveys()
    }

    fun getUserSurveys(loginId: String): List<Survey>? {
        return surveyRepository.findByUserLoginId(loginId)
    }

    fun getQuestions(): List<Question>? {
        return questionRepository.findAll()
    }

    fun mergeSurvey(survey: Survey) {
        surveyRepository.save(survey)
    }

    fun findQuestionOptionById(questionOptionId: Long): QuestionOption? {
        return questionOptionRepository.findById(questionOptionId).orElse(null)
    }

    fun findParticipatedSurveysByLoginId(loginId: String): List<Survey> {
        return surveyRepository.findParticipatedSurveyByLoginId(loginId)
    }

    @Transactional
    fun deleteSurvey(survey: Survey) {
        surveyRepository.delete(survey)
    }

    fun getSurveyParticipationList(sessionLoginId: String): List<SurveyParticipation> {
        val user = userRepository.findByLoginId(sessionLoginId) ?: throw IllegalArgumentException("User not found")
        val userId = user.userId ?: throw IllegalArgumentException("User ID not found")
        return surveyParticipationRepository.findByUserId(userId)
    }

    fun getParticipationBySurveyId(surveyId: Long): SurveyParticipation {
        return surveyParticipationRepository.findBySurveyId(surveyId)
    }

    fun getParticipationById(participationId: Long): SurveyParticipation {
        return surveyParticipationRepository.findById(participationId).orElse(null)
    }

    fun getQuestionsBySurveyId(surveyId: Long?): List<Question> {
        return questionRepository.findBySurveyId(surveyId)
    }

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
                QuestionType.MULTIPLE_CHOICE -> ChoiceAnswer(null,user,question,surveyParticipation,
                    AnswerType.MULTIPLE_CHOICE,findQuestionOptionById(alf.selectedOption!!))
                QuestionType.SUBJECTIVE -> TextAnswer(null,user,question,surveyParticipation,AnswerType.SUBJECTIVE,alf.text)
                else -> { throw IllegalArgumentException("Invalid Question Type")}
            }
            question.answers.add(answer) // 각 question에 answer 추가
        }

        surveyParticipationRepository.save(surveyParticipation) // SurveyParticipation 저장
       // surveyRepository.merge(survey) // mergeSurvey()를 통해 survey 업데이트

        return survey
    }

    fun getRemainingDays(survey: Survey?): Any {
        if(survey == null) return 0
        return survey.endDate!!.toEpochDay() - LocalDate.now().toEpochDay()

    }

}
