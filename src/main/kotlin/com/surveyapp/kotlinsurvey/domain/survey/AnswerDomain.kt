package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.UserDomain
import jakarta.persistence.*

@Entity
class AnswerDomain(

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val answer_id: Long? = null, // 설문 조사 질문 번호 == 답변할 질문 번호

        @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "member_id")
    val join_user : UserDomain, // 설문 조사 응답자

        @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    val question: QuestionDomain, // 질문

        @Column
    @Enumerated(EnumType.STRING)
    val answer_type: AnswerTypeDomain, // 설문 조사 질문 유형 == 답변 유형 : 주관식, 객관식

    // class answer : class ObjectiveAnswer 를 1:n mapping
        @OneToMany(mappedBy = "answer", cascade = [CascadeType.ALL], orphanRemoval = true)
    val objective_answer: MutableList<ObjectiveAnswerDomain> = mutableListOf(), // 객관식 답변 목록

        val subjective_answer: String? = null // 주관식 답변
) {

}
