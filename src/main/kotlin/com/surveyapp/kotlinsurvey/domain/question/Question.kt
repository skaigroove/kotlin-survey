/* Question.kt
* SurveyBay - 설문 속 질문 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.2.
*/

package com.surveyapp.kotlinsurvey.domain.question

import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.answer.Answer
import jakarta.persistence.*

@Entity
@Table(name = "tb_question")
class Question(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val questionId: Long, // 질문 Id

    @ManyToOne
    @JoinColumn(name = "survey_id")
    val survey: Survey, // 설문 하나당 질문 여러 개 존재 가능

    @Column(name = "context", nullable = false)
    val context: String, // 질문 내용

    @Column(name = "question_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val questionType: QuestionType, // 질문 유형

) {
    // 1:n = question: answers
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers: MutableList<Answer> = mutableListOf() // 질문 하나당 다변 여러 개 존재 가능

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var questionOptions : MutableList<QuestionOption> = mutableListOf() // 질문 하나당 선택지 여러 개 존재 가능

    fun addAnswer(answer: Answer) {
        answers.add(answer)
    }
}
