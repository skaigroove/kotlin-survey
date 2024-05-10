package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.answer.AnswerType

class AnswerForm {
    var questionId : Long? = null
    var selectedOptionId : Long? = null
    var text : String? = null
    var answerType : AnswerType = AnswerType.NONE
}