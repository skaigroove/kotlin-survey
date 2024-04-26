package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Answer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    open val answer_id: Long? = null, // 설문 내 질문 번호

    @ManyToOne
    @JoinColumn(name="member_id")
    open val join_user : User, // 멤버 하나 당 여러개의 답변이 있을 수 있다.

    @ManyToOne
    @JoinColumn(name = "question_id")
    open val question: Question, // 질문 하나에 대한 여러개의 답변이 있을 수 있다.
)
