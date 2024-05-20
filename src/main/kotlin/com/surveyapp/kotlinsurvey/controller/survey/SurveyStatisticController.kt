package com.surveyapp.kotlinsurvey.controller.survey

import com.surveyapp.kotlinsurvey.domain.answer.AnswerType
import com.surveyapp.kotlinsurvey.service.SurveyService
import jakarta.persistence.EntityManager
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/home")
class SurveyStatisticController(@Autowired private val em: EntityManager,
    @Autowired private val surveyService: SurveyService,
) {

    @GetMapping("api/list/statistic/{surveyId}")
    fun getSurveyStatistic(@PathVariable surveyId: Long, session: HttpSession): ResponseEntity<Map<String, Any>> {
        val multipleChoiceStatistics = getSurveyStatisticsMultipleChoice(surveyId)
        val subjectiveStatistics = getSurveyStatisticsSubjective(surveyId)

        val survey = surveyService.getSurveyById(surveyId) ?: return ResponseEntity.notFound().build()
        val username = session.getAttribute("username") as String? ?: "Unknown User"

        val result = mapOf(
            "objective" to multipleChoiceStatistics,
            "subjective" to subjectiveStatistics,
            "survey" to mapOf(
                "title" to survey.title,
                "startDate" to survey.startDate.toString(),
                "endDate" to survey.endDate.toString(),
                "description" to survey.description
            ),
            "username" to username
        )

        return ResponseEntity.ok(result)
    }



    fun getSurveyStatisticsMultipleChoice(surveyId: Long): List<Map<String, Any>> {
        val results = em.createQuery(
            "SELECT q.context, op.questionOptionText, COUNT(so) FROM Question q JOIN q.questionOptions op LEFT JOIN op.choiceAnswers so WHERE so.surveyParticipation.survey.surveyId = :surveyId GROUP BY q.questionId, op.questionOptionId",
            Array<Any>::class.java
        )
            .setParameter("surveyId", surveyId)
            .resultList

        return results.map {
            mapOf(
                "question" to (it[0] as? String ?: ""),
                "option" to (it[1] as? String ?: ""),
                "count" to (it[2] as? Long ?: 0L)
            )
        }
    }

    fun getSurveyStatisticsSubjective(surveyId: Long): Map<String, List<String>> {
        val results = em.createQuery(
            "SELECT q.context, a.text FROM TextAnswer a JOIN a.question q WHERE a.surveyParticipation.survey.surveyId = :surveyId AND a.answerType = 'SUBJECTIVE'",
            Array<Any>::class.java
        )
            .setParameter("surveyId", surveyId)
            .resultList

        return results.groupBy(
            { (it[0] as? String).orEmpty() }, // q.context
            { (it[1] as? String).orEmpty() }  // a.text
        )
    }
}

