package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.survey.Survey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SurveyRepository : JpaRepository<Survey, Long> {

    @Query("select s from Survey s where s.user.loginId = :loginId")
    fun findByUserLoginId(@Param("loginId") loginId: String): List<Survey>?

    @Query("select s from Survey s")
    fun findAllSurveys(): List<Survey>?

    @Query("select p.survey from SurveyParticipation p where p.user.loginId = :loginId")
    fun findParticipatedSurveyByLoginId(@Param("loginId") loginId: String): List<Survey>
}
