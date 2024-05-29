package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import com.surveyapp.kotlinsurvey.repository.SurveyParticipationRepository
import org.springframework.beans.factory.annotation.Autowired

class SurveyParticipationService(
    @Autowired private val surveyParticipationRepository: SurveyParticipationRepository,
    ) {
    fun addParticipation(surveyParticipation: SurveyParticipation) {
       surveyParticipationRepository.saveParticipation(surveyParticipation)
    }

    fun getSurveyParticipationListByUserId(userId: Long): List<SurveyParticipation>? { // userId 를 가진 사용자가 참여한 설문 조사 목록을 반환함
        return surveyParticipationRepository.getSurveyParticipationListByUserId(userId)
    }
    
    fun getSurveyParticipationListBySurveyId(surveyId: Long): List<SurveyParticipation> {
        return surveyParticipationRepository.getSurveyParticipationListBySurveyId(surveyId)
    }


}