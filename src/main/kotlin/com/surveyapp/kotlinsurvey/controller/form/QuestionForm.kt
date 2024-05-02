package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.survey.QuestionType
data class QuestionForm(
    var context: String = "",
    var questionType: QuestionType = QuestionType.SUBJECTIVE, // 기본값으로 주관식 설정
    var options: MutableList<String> = mutableListOf() // 객관식 질문의 옵션들
)