package com.surveyapp.kotlinsurvey.domain.survey

import jakarta.persistence.*

@Entity
class ObjectiveAnswerDomain(
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val objective_id: Long? = null, // 객관식 질문 번호

        @ManyToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "answer_id")
    val answer: AnswerDomain, // 객관식 응답

        @Column
    val option: String, // 객관식 답변 내용
) {

}
