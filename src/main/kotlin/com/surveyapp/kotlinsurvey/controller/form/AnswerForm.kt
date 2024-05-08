package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.answer.AnswerType

class AnswerForm {
    val questionId : Long? = null
    val selectedOptionId : Long? = null
    val text : String? = null
    val answerType : AnswerType = AnswerType.NONE
}