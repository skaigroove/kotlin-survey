package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.survey.QuestionType

data class QuestionForm(
    var context: String = "default",
    var questionType: QuestionType = QuestionType.SUBJECTIVE,
    var options: MutableList<String> = mutableListOf() // 객관식 질문의 옵션들
)