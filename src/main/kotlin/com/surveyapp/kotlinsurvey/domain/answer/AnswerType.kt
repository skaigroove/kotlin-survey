/* AnswerType.kt
* SurveyBay - 답변 유형 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.4.
*/


package com.surveyapp.kotlinsurvey.domain.answer

enum class AnswerType(str : String) {
    OBJECTIVE("OBJECTIVE"), // 객관식
    SUBJECTIVE("SUBJECTIVE"), // 주관식
    NONE("NONE") // 선택 없음
}