package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
class Answer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val answer_id: Long? = null,

    @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "member_id")
    val join_user : User, // 설문에 답변한 유저

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    val question: Question,

    @Column
    @Enumerated(EnumType.STRING)
    val answer_type: AnswerType,

    @OneToMany(mappedBy = "answer", cascade = [CascadeType.ALL], orphanRemoval = true)
    val objective_answer: MutableList<ObjectiveAnswer> = mutableListOf(), // 객관식 답변 리스트

    val subjective_answer: String? = null // 주관식 답변
) {

}