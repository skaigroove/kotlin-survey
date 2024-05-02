package com.surveyapp.kotlinsurvey.domain.survey

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

    @Column(name = "question_type")
    @Enumerated(EnumType.STRING)
    val questionType: QuestionType, // 질문 유형

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = [JoinColumn(name = "question_id")])
    @Column(name = "option_value")
    var questionOptions: MutableList<String> = mutableListOf() // 각 선지를 리스트로 받음

) {
    // 1:n = question: answers
    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers: MutableList<Answer> = mutableListOf() // 질문 하나 당 여러개의 답변을 가질 수 있다.
}
