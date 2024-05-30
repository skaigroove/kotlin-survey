package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import com.surveyapp.kotlinsurvey.repository.SurveyParticipationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SurveyParticipationService(private val surveyParticipationRepository: SurveyParticipationRepository) {

    @Transactional
    fun saveParticipation(surveyParticipation: SurveyParticipation) {
        surveyParticipationRepository.save(surveyParticipation)
    }

    fun getSurveyParticipationListByUserId(userId: Long): List<SurveyParticipation>? {
        return surveyParticipationRepository.findByUserId(userId)
    }

    fun findParticipationById(participationId: Long): SurveyParticipation? {
        return surveyParticipationRepository.findById(participationId).orElse(null)
    }
}
