package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.controller.form.AnswerForm
import com.surveyapp.kotlinsurvey.controller.form.AnswerListForm
import com.surveyapp.kotlinsurvey.domain.answer.Answer
import com.surveyapp.kotlinsurvey.domain.answer.AnswerType
import com.surveyapp.kotlinsurvey.domain.answer.ChoiceAnswer
import com.surveyapp.kotlinsurvey.domain.answer.TextAnswer
import com.surveyapp.kotlinsurvey.domain.question.Question
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
    @Autowired private val surveyParticipationRepository: SurveyParticipationRepository
) {

    fun saveSurvey(survey: Survey) {
        surveyRepository.saveSurvey(survey)
    }

    fun getSurveyById(surveyId: Long): Survey? { return surveyRepository.getSurveyById(surveyId) }

    fun getSurveyList(): List<Survey>? { return surveyRepository.getSurveyList() }

    fun getQuestionList(): List<Question>? { return surveyRepository.getQuestionList() }

    fun participateSurvey(survey: Survey, answerListForm: AnswerListForm) : Survey {

        println("Answer List Size: ${answerListForm.answerList.size}")
        answerListForm.answerList.forEach { answerForm ->
            println("Answer Question ID: ${answerForm.questionId}, Selected Option ID: ${answerForm.selectedOptionId}, Text: ${answerForm.text}")
        }

        // survey에 answerForm을 통해 받은 답변을 추가
        for( alf in answerListForm.answerList) { // answerListForm을 통해 받은 답변을 survey에 추가

            val question = survey.findQuestion(alf.questionId) // questionId를 통해 question을 찾음

            val surveyParticipation = SurveyParticipation(null,survey.user,survey) // SurveyParticipation 객체 생성

            surveyParticipationRepository.saveParticipation(surveyParticipation) // SurveyParticipation 저장

            val answer : Answer = when(question?.questionType) { // questionType에 따라 다른 Answer 객체 생성
                QuestionType.MULTIPLE_CHOICE -> ChoiceAnswer(null,survey.user,question,surveyParticipation,AnswerType.MULTIPLE_CHOICE,alf.selectedOptionId)
                QuestionType.SUBJECTIVE -> TextAnswer(null,survey.user,question,surveyParticipation,AnswerType.SUBJECTIVE,alf.text)
                else -> { throw IllegalArgumentException("Invalid Question Type") }
            }
            question.answers.add(answer) // 각 question에 answer 추가
        }

        surveyRepository.mergeSurvey(survey) // mergeSurvey()를 통해 survey 업데이트

        return survey
    }

}