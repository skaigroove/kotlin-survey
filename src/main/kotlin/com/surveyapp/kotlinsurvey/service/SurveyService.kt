package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.repository.SurveyRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Transactional
class SurveyService(@Autowired private val surveyRepository: SurveyRepository) {

    @Transactional
    fun saveSurvey(survey: Survey) {
        surveyRepository.saveSurvey(survey)
    }

    fun getSurveyById(surveyId: Long): Survey? { return surveyRepository.getSurveyById(surveyId) }

    fun getSurveyList(): List<Survey>? { return surveyRepository.getSurveyList() }

}