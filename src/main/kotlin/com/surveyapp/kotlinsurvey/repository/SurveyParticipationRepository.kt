package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SurveyParticipationRepository : JpaRepository<SurveyParticipation, Long> {

    @Query("select p from SurveyParticipation p where p.user.userId = :userId")
    fun findByUserId(@Param("userId") userId: Long): List<SurveyParticipation>

    @Query("select p from SurveyParticipation p where p.survey.surveyId = :surveyId")
    fun findBySurveyId(@Param("surveyId") surveyId: Long): SurveyParticipation
}
