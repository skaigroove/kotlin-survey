package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.controller.form.AnswerListForm
import com.surveyapp.kotlinsurvey.controller.form.EditAnswerForm
import com.surveyapp.kotlinsurvey.controller.form.EditAnswerListForm
import com.surveyapp.kotlinsurvey.controller.form.SurveyForm
import com.surveyapp.kotlinsurvey.domain.answer.Answer
import com.surveyapp.kotlinsurvey.domain.answer.AnswerType
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

    fun getSurveyById(surveyId: Long): Survey? {
        return surveyRepository.getSurveyById(surveyId)
    }

    fun getSurveyList(): List<Survey>? {
        return surveyRepository.getSurveyList()
    }

    fun getUserSurveyList(loginId: String): List<Survey>? {
        return surveyRepository.getUserSurveyList(loginId)
    }

    fun createSurvey(surveyForm: SurveyForm, user: User): Survey { // 설문 조사 생성 함수

        // 받아 온 surveyForm 의 값을 Survey 생성자에 넣어 survey 생성
        val survey = Survey(
            0, user, surveyForm.title, surveyForm.description, surveyForm.startDate, surveyForm.endDate
        )

        val questions = createQuestions(surveyForm, survey) // 질문을 surveyForm의 questionForm으로부터 생성하고 Repository에 저장

        // Survey 객체에 질문 리스트 설정
        survey.questions.addAll(questions)

        return survey
    }

    private fun createQuestions(form: SurveyForm, survey: Survey): MutableList<Question> {
        return form.questions.map { questionForm ->
            val question = Question(
                questionId = 0,  // 신규 생성이므로 ID는 null
                survey = survey,    // 부모 설문조사 객체 연결
                context = questionForm.context,  // 질문 내용
                questionType = questionForm.questionType  // 질문 유형
            )

            var index = 1

            // 객관식 질문일 경우 선택지를 추가
            if (questionForm.questionType == QuestionType.OBJECTIVE) {
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

    fun participateSurvey(survey: Survey, loginId: String, answerListForm: AnswerListForm): Survey {

        val user = userService.findUserByLoginId(loginId)
        if (user == null)
            throw IllegalArgumentException("participate User not found")

        val surveyParticipation = SurveyParticipation(null, user, survey, LocalDate.now()) // SurveyParticipation 객체 생성

        // survey에 answerForm을 통해 받은 답변을 추가
        for (alf in answerListForm.answerList) { // answerListForm을 통해 받은 답변을 survey에 추가

            val question = survey.findQuestion(alf.questionId) // questionId를 통해 question을 찾음

            val answer: Answer = when (question?.questionType) { // questionType에 따라 다른 Answer 객체 생성
                QuestionType.OBJECTIVE -> Answer(
                    null,
                    AnswerType.OBJECTIVE,
                    user,
                    question,
                    surveyParticipation,
                    findQuestionOptionById(alf.selectedOption!!),
                    null
                )

                QuestionType.SUBJECTIVE -> Answer(
                    null,
                    AnswerType.SUBJECTIVE,
                    user,
                    question,
                    surveyParticipation,
                    null,
                    alf.text!!
                )

                else -> {
                    throw IllegalArgumentException("Invalid Question Type")
                }
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

    fun getRemainingDays(survey: Survey?): Any {
        if (survey == null) return 0
        return survey.endDate!!.toEpochDay() - LocalDate.now().toEpochDay()

    }

    fun getParticipatedSurveyIds(loginId: String): List<Long> {
        return surveyRepository.findParticipatedSurveysByLoginId(loginId)
    }

    fun deleteSurvey(surveyId: Long) {
        val survey = surveyRepository.getSurveyById(surveyId) ?: throw IllegalArgumentException("Survey not found")
        surveyRepository.deleteSurvey(survey)
    }

    fun getQuestionOptionByQuestionId(questionId: Long): QuestionOption? {
        return surveyRepository.getQuestionOptionByQuestionId(questionId)
    }

    fun getParticipationById(participationId: Long): SurveyParticipation? {
        return surveyParticipationRepository.getParticipationById(participationId)

    }

    fun getAnswerListFormByParticipation(surveyParticipation: SurveyParticipation): EditAnswerListForm {

        // participation Id로 답변들을 찾아온다
        val answers = surveyRepository.findAnswersByParticipation(surveyParticipation)

        // 받아온 기존 answers들을 EditAnswerForm으로 변환하여 default값으로 설정
        val editAnswerForms: List<EditAnswerForm> = answers.map { answer ->
            EditAnswerForm().apply {
                answerType = answer.answerType
                questionId = answer.question.questionId
                objectiveAnswer = answer.objectiveAnswer?.questionOptionId
                subjectiveAnswer = answer.subjectiveAnswer
                question = answer.question
            }
        }

        // 새로운 EditAnswerListForm을 생성하여 EditAnswerForm들을 설정
        val editAnswerListForm: EditAnswerListForm = EditAnswerListForm()
        editAnswerListForm.editAnswerList = editAnswerForms

        return editAnswerListForm
    }

    //    @Transactional
//    fun updateAnswers(surveyParticipation: SurveyParticipation, editAnswerListForm: EditAnswerListForm) {
//        val existingAnswers = surveyRepository.findAnswersByParticipation(surveyParticipation)
//
//        // 기존 답변을 map으로 변환하여 질문 ID를 키로 사용
//        val existingAnswerMap = existingAnswers.associateBy { it.question.questionId }
//
//        // 새로운 답변 리스트 생성
//        val updatedAnswers = (editAnswerListForm.editAnswerList ?: emptyList()).map { answerForm ->
//            val existingAnswer = existingAnswerMap[answerForm.questionId]
//
//            if (existingAnswer != null) {
//                // 기존 답변이 있는 경우 업데이트
//                existingAnswer.answerType = answerForm.answerType
//                existingAnswer.objectiveAnswer = answerForm.objectiveAnswer?.let { surveyRepository.findQuestionOptionById(it) }
//                existingAnswer.subjectiveAnswer = answerForm.subjectiveAnswer
//                existingAnswer
//            } else {
//                // 기존 답변이 없는 경우 새로운 답변 생성
//                Answer(
//                    question = surveyRepository.findQuestionById(answerForm.questionId)!!,
//                    answerType = answerForm.answerType,
//                    objectiveAnswer = answerForm.objectiveAnswer?.let { surveyRepository.findQuestionOptionById(it) },
//                    subjectiveAnswer = answerForm.subjectiveAnswer,
//                    user = surveyParticipation.user,
//                    surveyParticipation = surveyParticipation
//                )
//            }
//        }
//
//        // 업데이트된 답변을 저장하거나 새로 추가
//        updatedAnswers.forEach { answer ->
//            surveyRepository.saveAnswer(answer)
//        }
//    }
    @Transactional
    fun updateAnswers(surveyParticipation: SurveyParticipation, editAnswerListForm: EditAnswerListForm) {
        println("updateAnswers 시작")

        val existingAnswers = surveyRepository.findAnswersByParticipation(surveyParticipation)
        println("기존 답변: $existingAnswers")

        // 기존 답변을 map으로 변환하여 질문 ID를 키로 사용
        val existingAnswerMap = existingAnswers.associateBy { it.question.questionId }
        println("기존 답변 맵: $existingAnswerMap")

        // editAnswerListForm.editAnswerList가 null인 경우 빈 리스트로 초기화
        val answerList = editAnswerListForm.editAnswerList ?: emptyList()
        println("새로운 답변 리스트: $answerList")

        // 새로운 답변 리스트 생성
        val updatedAnswers = answerList.map { answerForm ->
            val existingAnswer = existingAnswerMap[answerForm.questionId]
            println("처리 중인 답변 폼: $answerForm")

            if (existingAnswer != null) {
                // 기존 답변이 있는 경우 업데이트
                println("기존 답변 업데이트: $existingAnswer")
                existingAnswer.answerType = answerForm.answerType
                existingAnswer.objectiveAnswer =
                    answerForm.objectiveAnswer?.let { surveyRepository.findQuestionOptionById(it) }
                existingAnswer.subjectiveAnswer = answerForm.subjectiveAnswer
                existingAnswer
            } else {
                // 기존 답변이 없는 경우 새로운 답변 생성
                println("새 답변 생성")
                Answer(
                    question = surveyRepository.findQuestionById(answerForm.questionId)!!,
                    answerType = answerForm.answerType,
                    objectiveAnswer = answerForm.objectiveAnswer?.let { surveyRepository.findQuestionOptionById(it) },
                    subjectiveAnswer = answerForm.subjectiveAnswer,
                    user = surveyParticipation.user,
                    surveyParticipation = surveyParticipation
                )
            }
        }
        println("업데이트된 답변 리스트: $updatedAnswers")

        // 업데이트된 답변을 저장하거나 새로 추가
        updatedAnswers.forEach { answer ->
            println("저장 중인 답변: $answer")
            surveyRepository.saveAnswer(answer)
        }

        println("updateAnswers 완료")
    }
}

