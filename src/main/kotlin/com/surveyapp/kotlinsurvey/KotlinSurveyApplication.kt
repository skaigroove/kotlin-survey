package com.surveyapp.kotlinsurvey

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class KotlinSurveyApplication

fun main(args: Array<String>) {
	runApplication<KotlinSurveyApplication>(*args)
}
