package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.answer.AnswerType
import com.surveyapp.kotlinsurvey.domain.question.QuestionOption

class AnswerForm {
    var questionId : Long? = null
    var selectedOption : Long? = null
    var text : String? = null
    var answerType : AnswerType = AnswerType.NONE
}