package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.question.Question
import com.surveyapp.kotlinsurvey.domain.question.QuestionOption
import com.surveyapp.kotlinsurvey.domain.survey.Survey
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
@Transactional
class SurveyRepository (

    @PersistenceContext // 생성자 주입 자동화 어노테이션
    private val em: EntityManager, // 생성자 주입
){
    @Transactional
    fun saveSurvey(survey: Survey) {
        em.persist(survey)
    }

    /* id를 기준으로 설문을 조회 (id가 unique 하므로, 단일 조회) */
    fun getSurveyById(id: Long): Survey? {
        return em.find(Survey::class.java, id) // EntityManager를 사용하기 때문에, java class로 적어준다
    }

    /* 모든 설문 조사 조회*/
    fun getSurveyList(): List<Survey>? {
        return em.createQuery("select m from Survey m", Survey::class.java)
            .getResultList()
    }

    fun getQuestionList(): List<Question>? {
        return em.createQuery("select m from Question m", Question::class.java)
            .getResultList()
    }

    fun mergeSurvey(survey: Survey) {
        em.merge(survey) // merge() : 이미 영속성 컨텍스트에 존재하는 엔티티를 수정
    }

    fun findQuestionOptionById(questionOptionId: Long): QuestionOption? {
        return em.find(QuestionOption::class.java, questionOptionId)
    }

    fun getSurveyParticipation(surveyId: Long): List<Any> {
        return em.createQuery(
            "select count(p) from SurveyParticipation p join p.answers a where p.survey.surveyId = :surveyId ",
            Array<Any>::class.java
        )
            .setParameter("surveyId", surveyId)
            .resultList
    }

    fun getSurveyStatisticsMultipleChoice(surveyId: Long): List<Any> {
        return em.createQuery(
            "SELECT op.questionOptionText, COUNT(so) FROM QuestionOption op LEFT JOIN op.choiceAnswers so WHERE so.surveyParticipation.survey.surveyId = :surveyId GROUP BY op.questionOptionText",
            Array<Any>::class.java
        )
            .setParameter("surveyId", surveyId)
            .resultList
    }
}