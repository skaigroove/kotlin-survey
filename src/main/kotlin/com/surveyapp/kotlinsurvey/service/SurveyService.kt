package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.repository.SurveyRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Transactional
class SurveyService(@Autowired private val surveyRepository: SurveyRepository) {

    fun saveSurvey(survey: Survey) {
        val newSurvey = Survey(survey.surveyId, survey.user, survey.title, survey.discription, survey.startDate, survey.endDate)
        surveyRepository.saveSurvey(newSurvey)
    }

    fun getSurvey(surveyId: Long): Survey? { return surveyRepository.getSurvey(surveyId) }

    fun getSurveyList(): List<Survey>? { return surveyRepository.getSurveyList() }

}