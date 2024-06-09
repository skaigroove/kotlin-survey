/* UserType.kt
* SurveyBay - 사용자 유형 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.5.4.
*/

package com.surveyapp.kotlinsurvey.domain.user

enum class UserType(str : String) {
    CLIENT("CLIENT"), // 고객 (회원)
    ADMIN("ADMIN") // 관리자
}