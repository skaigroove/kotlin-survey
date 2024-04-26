package com.surveyapp.kotlinsurvey.domain.survey

import jakarta.persistence.*

@Entity
class Choice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val choiceId: Long? = null, // 객관식 옵션 번호

    @Column(nullable = false)
    val text: String, // 객관식 옵션 텍스트

    @ManyToOne
    @JoinColumn(name = "question_id")
    val question: Question, // 이 객관식 옵션이 속한 질문
) {

}