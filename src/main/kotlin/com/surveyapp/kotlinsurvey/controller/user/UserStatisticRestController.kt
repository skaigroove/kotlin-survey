/* UserStatisticRestController.kt
* SurveyBay - 회원 : 설문 결과 관련 Controller
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.4. 디자인 + 통계 : 객관식 질문 내용 나오도록 출력
*/

package com.surveyapp.kotlinsurvey.controller.user

import com.surveyapp.kotlinsurvey.domain.question.QuestionType
import com.surveyapp.kotlinsurvey.repository.SurveyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/home/api/list/statistic")
class SurveyStatisticController(
    @Autowired private val surveyRepository: SurveyRepository
) {

    @GetMapping("/{surveyId}")
    fun getSurveyStatistics(@PathVariable surveyId: Long): SurveyStatisticsResponse { // 설문 결과 처리
        val survey = surveyRepository.getSurveyById(surveyId) ?: throw RuntimeException("Survey not found")
        val questions = survey.questions.sortedBy { it.questionId }

        val objectiveStatistics = mutableListOf<ObjectiveStatisticResponse>()
        val subjectiveStatistics = mutableMapOf<Long, MutableList<String>>()
        val subjectiveQuestions = mutableListOf<SubjectiveQuestionResponse>() // 주관식 질문 리스트

        for (question in questions) {
            if (question.questionType == QuestionType.OBJECTIVE) { // 객과닉 질문 처리
                val options = question.questionOptions
                for (option in options) {
                    val count = option.objectiveAnswers.size
                    objectiveStatistics.add(
                        ObjectiveStatisticResponse(
                            questionId = question.questionId,
                            question = question.context,
                            option = option.questionOptionText,
                            count = count
                        )
                    )
                }
            } else if (question.questionType == QuestionType.SUBJECTIVE) { // 주관식 질문 처리
                val answers = question.answers.mapNotNull { it.subjectiveAnswer }
                subjectiveStatistics[question.questionId] = answers.toMutableList()
                subjectiveQuestions.add(
                    SubjectiveQuestionResponse(
                        questionId = question.questionId,
                        question = question.context
                    )
                )
            }
        }

        return SurveyStatisticsResponse(
            username = "사용자", // 실제 사용자 이름을 설정해야 합니다.
            survey = SurveyResponse(
                surveyId = survey.surveyId,
                title = survey.title,
                startDate = survey.startDate.toString(),
                endDate = survey.endDate.toString(),
                description = survey.description
            ),
            objective = objectiveStatistics,
            subjective = subjectiveStatistics,
            subjectiveQuestions = subjectiveQuestions // 주관식 질문 포함
        )
    }
}
data class SurveyStatisticsResponse(
    val username: String,
    val survey: SurveyResponse,
    val objective: List<ObjectiveStatisticResponse>,
    val subjective: Map<Long, List<String>>,
    val subjectiveQuestions: List<SubjectiveQuestionResponse> // 주관식 질문 추가
)

data class SurveyResponse(
    val surveyId: Long,
    val title: String,
    val startDate: String,
    val endDate: String,
    val description: String
)

data class ObjectiveStatisticResponse(
    val questionId: Long,
    val question: String,
    val option: String,
    val count: Int
)

data class SubjectiveQuestionResponse(
    val questionId: Long,
    val question: String
)