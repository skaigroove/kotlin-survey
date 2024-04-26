package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
class Question(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val question_id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "survey_id")
    val survey: Survey? = null, // 설문 하나 당 여러 개의 질문 가질 수 있다.

    // mapping
    // 1:n = question: answers
    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers : MutableList<Answer> = mutableListOf(), // 질문 하나 당 여러개의 답변을 가질 수 있다.

    // mapping
    // 1:n = question: choices
    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    @Column(nullable = false)
    val choices : MutableList<Choice> = mutableListOf(), // 질문 하나 당 여러개의 답변을 가질 수 있다.

    @Column(name = "context", nullable = false)
    val context: String, // 컨텍스트 영역
) {


}
