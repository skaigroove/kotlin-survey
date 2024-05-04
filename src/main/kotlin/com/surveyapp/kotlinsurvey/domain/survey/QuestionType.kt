package com.surveyapp.kotlinsurvey.domain.survey

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

enum class QuestionType {
    MULTIPLE_CHOICE,
    SUBJECTIVE
}