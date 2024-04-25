package com.surveyapp.kotlinsurvey.DTO.Survey

import com.surveyapp.kotlinsurvey.domain.survey.SurveyDomain
import com.surveyapp.kotlinsurvey.domain.user.UserDomain
import java.util.*
import java.util.stream.DoubleStream.builder


data class SurveyDTO(
        val survey_id: Int?,
        val user: UserDomain,
        val title: String,
        val discription: String,
        val start_date: Date,
        val end_date: Date
)
{
    fun toEntity(): SurveyDomain {

    }
}
