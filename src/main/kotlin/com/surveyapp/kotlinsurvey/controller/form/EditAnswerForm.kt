package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.answer.AnswerType
import com.surveyapp.kotlinsurvey.domain.question.Question

class EditAnswerForm{
    var question : Question? = null
    var questionId : Long? = null
    var objectiveAnswer : Long? = null
    var subjectiveAnswer : String? = null
    var answerType : AnswerType = AnswerType.NONE
}