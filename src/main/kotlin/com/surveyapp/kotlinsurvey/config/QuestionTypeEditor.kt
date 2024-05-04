package com.surveyapp.kotlinsurvey.config

import com.surveyapp.kotlinsurvey.domain.survey.QuestionType
import java.beans.PropertyEditorSupport

class QuestionTypeEditor : PropertyEditorSupport() {
    override fun setAsText(text: String?) {
        text?.let {
            value = QuestionType.valueOf(it.uppercase())
        }
    }
}
