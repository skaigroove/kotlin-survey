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
    fun saveSurvey(survey: Survey) {
        em.persist(survey)
    }

    fun getSurveyById(id: Long): Survey? {
        return em.find(Survey::class.java, id)
    }

    fun getSurveyList(): List<Survey>? {
        return em.createQuery("select m from Survey m", Survey::class.java).resultList
    }

    fun getUserSurveyList(loginId: String): List<Survey>? {
        return em.createQuery(
            "select s from Survey s where s.user.loginId = :loginId",
            Survey::class.java
        )
            .setParameter("loginId", loginId)
            .resultList
    }

    fun getQuestionList(): List<Question>? {
        return em.createQuery("select m from Question m", Question::class.java).resultList
    }

    fun mergeSurvey(survey: Survey) {
        em.merge(survey)
    }

    fun findQuestionOptionById(questionOptionId: Long): QuestionOption? {
        return em.find(QuestionOption::class.java, questionOptionId)
    }

    fun findParticipatedSurveysByLoginId(loginId: String): List<Long> {
        return em.createQuery(
            "select p.survey.surveyId from SurveyParticipation p where p.user.loginId = :loginId",
            java.lang.Long::class.java
        )
            .setParameter("loginId", loginId)
            .resultList
            .map { it.toLong() }
    }

    fun deleteSurvey(survey: Survey) {
        em.remove(survey)
    }

    fun getQuestionOptionByQuestionId(questionId: Long): QuestionOption? {
        return em.createQuery(
            "select qo from QuestionOption qo JOIN Answer a where a.question.questionId= :questionId and  qo.questionOptionId == a.objectiveAnswer.questionOptionId",
            QuestionOption::class.java
        )
            .setParameter("questionId", questionId)
            .singleResult
    }

    fun findAnswersByParticipation(surveyParticipation: SurveyParticipation): List<Answer> {
        return em.createQuery(
            "select a from Answer a where a.surveyParticipation.participationId = :participationId",
            Answer::class.java
        )
            .setParameter("participationId", surveyParticipation.participationId)
            .resultList

    }

    fun findQuestionById(questionId: Long?): Question? {
        return em.find(Question::class.java, questionId)

    }

    fun saveAnswer(answer: Answer) {
        if (answer.answerId == null) {
            em.persist(answer)
        } else {
            em.merge(answer)
        }
    }

}
