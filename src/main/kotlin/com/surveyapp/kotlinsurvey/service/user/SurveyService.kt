package com.surveyapp.kotlinsurvey.service.user

import com.surveyapp.kotlinsurvey.domain.survey.*
import com.surveyapp.kotlinsurvey.repository.SurveyRepository
import com.surveyapp.kotlinsurvey.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
@Transactional
class SurveyService(@Autowired private val surveyRepository: SurveyRepository) {

    fun saveSurvey(survey: Survey) {
        val newSurvey = Survey(survey.survey_id, survey.user, survey.title, survey.discription, survey.startDate, survey.endDate)
        surveyRepository.saveSurvey(newSurvey)
    }

    fun getSurvey(surveyId: Long?): Survey? { return surveyRepository.getSurvey(surveyId) }

    fun getSurveyList(): List<Survey>? { return surveyRepository.getSurveyList() }

}