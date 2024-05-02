package com.surveyapp.kotlinsurvey.controller.form

import java.time.LocalDate

data class SurveyForm(
    var title: String = "",
    var description: String = "",
    var startDate: LocalDate = LocalDate.now(),
    var endDate: LocalDate = LocalDate.now(),
    var questions: MutableList<QuestionForm> = mutableListOf()
)
