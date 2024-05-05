package com.surveyapp.kotlinsurvey.domain.question

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

enum class QuestionType(str : String) {
    MULTIPLE_CHOICE("MULTIPLE_CHOICE"), // 객관식
    SUBJECTIVE("SUBJECTIVE"), // 주관식
    NONE("NONE") // 선택없음
}