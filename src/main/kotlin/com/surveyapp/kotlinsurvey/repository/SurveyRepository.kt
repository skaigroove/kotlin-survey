/* SurveyRepository.kt
* SurveyBay - 설문 관련 Repository 클래스
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.4. 설문 응답 수정 부분 추가 처리
*/

package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.answer.Answer
import com.surveyapp.kotlinsurvey.domain.answer.AnswerType
import com.surveyapp.kotlinsurvey.domain.question.Question
import com.surveyapp.kotlinsurvey.domain.question.QuestionOption
import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class SurveyRepository(
    @PersistenceContext private val em: EntityManager
) {
    @Transactional
    fun saveSurvey(survey: Survey) { // 설문 저장
        em.persist(survey)
    }

    fun getSurveyById(id: Long): Survey? { // (survey)Id 에 해당하는 설문 반환
        return em.find(Survey::class.java, id)
    }

    fun getSurveyList(): List<Survey>? { // 모든 설문 목록 반환
        return em.createQuery("select m from Survey m", Survey::class.java).resultList
    }

    fun getUserSurveyList(loginId: String): List<Survey>? { // loginId - 회원이 작성한 모든 설문 목록 반환
        return em.createQuery(
            "select s from Survey s where s.user.loginId = :loginId",
            Survey::class.java
        )
            .setParameter("loginId", loginId)
            .resultList
    }

    fun getQuestionList(): List<Question>? { // 질문 목록 반환
        return em.createQuery("select m from Question m", Question::class.java).resultList
    }

    fun mergeSurvey(survey: Survey) {
        em.merge(survey)
    }

    fun findQuestionOptionById(questionOptionId: Long): QuestionOption? { // questionOptionId 에 해당하는 질문의 선지 반환
        return em.find(QuestionOption::class.java, questionOptionId)
    }

    fun findParticipatedSurveysByLoginId(loginId: String): List<Long> { // loginId - 회원이 참여한 설문 id 목록 반환
        return em.createQuery(
            "select p.survey.surveyId from SurveyParticipation p where p.user.loginId = :loginId",
            java.lang.Long::class.java
        )
            .setParameter("loginId", loginId)
            .resultList
            .map { it.toLong() }
    }

    fun deleteSurvey(survey: Survey) { // 설문 삭제
        em.remove(survey)
    }

    fun getQuestionOptionByQuestionId(questionId: Long): QuestionOption? { // questionOptionId 에 해당하는 질문의 선지 반환
        return em.createQuery(
            "select qo from QuestionOption qo JOIN Answer a where a.question.questionId= :questionId and  qo.questionOptionId == a.objectiveAnswer.questionOptionId",
            QuestionOption::class.java
        )
            .setParameter("questionId", questionId)
            .singleResult
    }

    fun findAnswersByParticipation(surveyParticipation: SurveyParticipation): List<Answer> { // 참여한 설문의 답변 반환
        return em.createQuery(
            "select a from Answer a where a.surveyParticipation.participationId = :participationId",
            Answer::class.java
        )
            .setParameter("participationId", surveyParticipation.participationId)
            .resultList

    }

    fun findQuestionById(questionId: Long?): Question? { // questionId 에 해당하는 질문 반환
        return em.find(Question::class.java, questionId)

    }

    fun saveAnswer(answer: Answer) { // 설문 답변 저장
        if (answer.answerId == null) {
            em.persist(answer)
        } else {
            em.merge(answer)
        }
    }

}
