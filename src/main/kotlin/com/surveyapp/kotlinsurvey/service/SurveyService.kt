package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.repository.SurveyRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SurveyService(@Autowired private val surveyRepository: SurveyRepository) {

    @Transactional
    fun saveSurvey(survey: Survey) {
        surveyRepository.saveSurvey(survey)
    }

    fun getSurvey(surveyId: Long): Survey? { return surveyRepository.getSurvey(surveyId) }

    fun getSurveyList(): List<Survey>? { return surveyRepository.getSurveyList() }

}