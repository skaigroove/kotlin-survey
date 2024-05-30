package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.answer.Answer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AnswerRepository : JpaRepository<Answer, Long> {

    fun findBySurveyParticipationParticipationId(participationId: Long): List<Answer>?

    @Query("select a from Answer a where a.answerId in :answerIdList")
    fun findByAnswerIdList(@Param("answerIdList") answerIdList: List<Long?>): List<Answer>?
}
