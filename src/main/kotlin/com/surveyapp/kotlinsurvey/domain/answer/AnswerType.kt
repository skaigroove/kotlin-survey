package com.surveyapp.kotlinsurvey.domain.answer

enum class AnswerType(str : String) {
    OBJECTIVE("OBJECTIVE"), // 객관식
    SUBJECTIVE("SUBJECTIVE"), // 주관식
    NONE("NONE") // 선택없음
}