package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.question.QuestionOption
import com.surveyapp.kotlinsurvey.domain.question.QuestionType

data class QuestionForm(
    var context: String = "default",
    var questionType: QuestionType = QuestionType.NONE,
    var questionOptions: MutableList<OptionForm> = mutableListOf() // 객관식 질문의 옵션들
)