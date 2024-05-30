package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.question.QuestionOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionOptionRepository : JpaRepository<QuestionOption, Long>
