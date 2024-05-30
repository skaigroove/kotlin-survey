package com.surveyapp.kotlinsurvey.domain.question

import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.answer.Answer
import com.surveyapp.kotlinsurvey.domain.answer.TextAnswer
import jakarta.persistence.*

@Entity
@Table(name = "tb_question")
class Question(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val questionId: Long?,

    @ManyToOne
    @JoinColumn(name = "survey_id")
    val survey: Survey, // 설문 하나 당 여러 개의 질문 가질 수 있다.

    @Column(name = "context", nullable = false)
    val context: String, // 컨텍스트 영역

    @Column(name = "question_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val questionType: QuestionType, // 질문 유형

) {
    // 1:n = question: answers
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers: MutableList<Answer> = mutableListOf() // 질문 하나 당 여러개의 답변을 가질 수 있다.

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var questionOptions : MutableList<QuestionOption> = mutableListOf() // 질문 하나 당 여러개의 선택지를 가질 수 있다.

    fun getTextAnswer(): String? {
        return answers.filterIsInstance<TextAnswer>().first().text
    }

    fun addAnswer(answer: Answer) {
        answers.add(answer)
    }
}
