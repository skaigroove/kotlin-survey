package com.surveyapp.kotlinsurvey.domain.survey

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table


@Entity
@Table(name = "tb_question_option")
class QuestionOption(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long?,

    @OneToOne
    @JoinColumn(name="question_id")
    val question: Question,

    @Column
    val option1: String?, // 객관식 선지 1

    @Column
    val option2: String?, // 객관식 선지 2

    @Column
    val option3: String?, // 객관식 선지 3

    @Column
    val option4: String?, // 객관식 선지 4

    @Column
    val option5: String?, // 객관식 선지 5
) {
}