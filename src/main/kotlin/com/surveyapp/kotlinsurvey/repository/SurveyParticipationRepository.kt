package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
@Transactional
class SurveyParticipationRepository(
    @PersistenceContext // 생성자 주입 자동화 어노테이션
    private val em: EntityManager, // 생성자 주입

) {
    @Transactional
    fun saveParticipation(surveyParticipation: SurveyParticipation) {
        em.persist(surveyParticipation)
    }

    fun getSurveyParticipationListByUserId(userId: Long): List<SurveyParticipation>? { // 해당 사용자가 참여한 설문 조사 목록 조회
        return em.createQuery(
            "SELECT s FROM SurveyParticipation s WHERE s.user.id = :userId",
            SurveyParticipation::class.java
        )
            .setParameter("userId", userId)
            .resultList
    }

    fun getSurveyParticipationListBySurveyId(surveyId: Long): List<SurveyParticipation> { // surveyId 이용 => SurveyParticipationList (참여자 관련 목록) 얻음 // 참여 x => 빈 리스트 반환
        return em.createQuery(
            "SELECT s FROM SurveyParticipation s WHERE s.survey.id = :surveyId",
            SurveyParticipation::class.java
        )
            .setParameter("surveyId", surveyId)
            .resultList
    }


}