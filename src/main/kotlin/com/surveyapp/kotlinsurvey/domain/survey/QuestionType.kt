package com.surveyapp.kotlinsurvey.domain.survey

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

enum class QuestionType(str : String) {
    MULTIPLE_CHOICE("MULTIPLE_CHOICE"), // 객관식 = 0
    SUBJECTIVE("SUBJECTIVE"), // 주관식 == 1
    NONE("NONE") // 선택없음 == 2
}