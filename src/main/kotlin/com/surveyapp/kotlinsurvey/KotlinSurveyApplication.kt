/* KotlinSurveyApplication.kt
* SurveyBay - kotlin-survey 실행
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.4.29
*/

package com.surveyapp.kotlinsurvey

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class KotlinSurveyApplication

fun main(args: Array<String>) {
	runApplication<KotlinSurveyApplication>(*args)
}
