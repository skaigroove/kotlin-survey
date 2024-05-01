package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.survey.Survey
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class SurveyRepository (

    @PersistenceContext // 생성자 주입 자동화 어노테이션
    private val em: EntityManager, // 생성자 주입
){
    fun saveSurvey(survey: Survey) {
        em.persist(survey)
    }

    /* id를 기준으로 설문을 조회 (id가 unique 하므로, 단일 조회) */
    fun getSurvey(id: Long): Survey? {
        return em.find(Survey::class.java, id) // EntityManager를 사용하기 때문에, java class로 적어준다
    }

    /* 모든 설문 조사 조회*/
    fun getSurveyList(): List<Survey>? {
        return em.createQuery("select m from Survey m", Survey::class.java)
            .getResultList()
    }

}