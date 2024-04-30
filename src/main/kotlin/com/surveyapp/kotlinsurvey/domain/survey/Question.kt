package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.UserType
import jakarta.persistence.*

@Entity
@Table(name = "tb_question")
class Question(

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val questionId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "survey_id")
    val survey: Survey? = null, // 설문 하나당 여러 개의 질문 가질 수 있다.

    @Column(name = "context", nullable = false)
    val context: String, // 내용

    @Column(name = "question_type")
    @Enumerated(EnumType.STRING)
    val questionType: QuestionType // 질문 유형


) {
    // 1:n = question: answers
    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers: MutableList<Answer> = mutableListOf() // 질문 하나 당 여러개의 답변을 가질 수 있다.

    @OneToOne(mappedBy = "question")
    val question_option: QuestionOption? = null
}
