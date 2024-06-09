/* GenderType.kt
* SurveyBay - 사용자 성별 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.5.4.
*/

package com.surveyapp.kotlinsurvey.domain.user

enum class GenderType(val desc:String) { // 성별
    MALE("남"), // 남자
    FEMALE("여") // 여자
}
