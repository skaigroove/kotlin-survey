package com.surveyapp.kotlinsurvey.domain.survey

import jakarta.persistence.*

@Entity
class ObjectiveAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val objective_id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "answer_id", referencedColumnName = "answer_id")
    val answer: Answer,

    @Column
    val option: String, // 객관식 답변 내용
) {

}