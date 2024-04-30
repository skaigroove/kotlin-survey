package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.domain.survey.QuestionType

data class QuestionRequest (
        val context: String, // 질문 내용
        val type: QuestionType, // 질문 유형 - 객관식, 주관식
        val option1: String?, // 객관식 선지 1
        val option2: String?, // 객관식 선지 2
        val option3: String?, // 객관식 선지 3
        val option4: String?, // 객관식 선지 4
        val option5: String? // 객관식 선지 5
)
