package com.surveyapp.kotlinsurvey.domain.survey

import jakarta.persistence.*


@Entity
@Table(name = "tb_question_option")
class QuestionOption(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id : Int?,

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
        val option5: String? // 객관식 선지 5
) {
}