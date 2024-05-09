package com.surveyapp.kotlinsurvey.domain.question

import jakarta.persistence.*

@Entity
@Table(name = "tb_question_option")
class QuestionOption(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val questionOptionId: Long? = null,

    @Column(name = "option_index")
    val optionIndex: Int,  // 선택지의 순서를 저장하는 필드

    @Column(name = "question_option")
    val questionOption: String,

    @ManyToOne
    @JoinColumn(name = "question_id")
    val question: Question
) {

}