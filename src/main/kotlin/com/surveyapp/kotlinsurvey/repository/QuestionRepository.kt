package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.question.Question
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : JpaRepository<Question, Long> {

    @Query("select q from Question q where q.survey.surveyId = :surveyId")
    fun findBySurveyId(@Param("surveyId") surveyId: Long?): List<Question>
}
