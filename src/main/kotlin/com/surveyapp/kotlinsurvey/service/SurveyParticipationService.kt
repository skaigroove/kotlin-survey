package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import com.surveyapp.kotlinsurvey.repository.SurveyParticipationRepository
import org.springframework.beans.factory.annotation.Autowired

class SurveyParticipationService(
    @Autowired private val surveyParticipationRepository: SurveyParticipationRepository
) {
    fun addParticipation(surveyParticipation: SurveyParticipation) {
       surveyParticipationRepository.saveParticipation(surveyParticipation)
    }
}