package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.domain.user.User
import java.time.LocalDate

data class SurveyRequest(
        val user: User, // 설문 조사 작성자 정보
        val title: String, // 설문 조사 제목
        val description: String, // 설문 조사 설명
        val startDate: LocalDate, // 설문 조사 시작일
        val endDate: LocalDate, // 설문 조사 종료일
        val questions: List<QuestionRequest> // 설문 조사에 포함된 질문 목록
)
