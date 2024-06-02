package com.surveyapp.kotlinsurvey.domain.question

enum class QuestionType(str : String) {
    OBJECTIVE("OBJECTIVE"), // 객관식
    SUBJECTIVE("SUBJECTIVE"), // 주관식
    NONE("NONE") // 선택없음
}