/* InquiryState.kt
* SurveyBay - 1:1 문의 글 상태 (답변 여부) 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.4.
*/

package com.surveyapp.kotlinsurvey.domain.inquiry

enum class InquiryState { // 문의 글 상태
    UNCOMPLETE, // 답변 미완료
    COMPLETE // 답변 완료
}