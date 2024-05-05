package com.surveyapp.kotlinsurvey.domain.answer

import com.surveyapp.kotlinsurvey.domain.question.Question
import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
@Table(name = "tb_answer")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Answer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val answerId: Long? = null, // 설문 내 질문 번호

    @ManyToOne
    @JoinColumn(name="user_id", foreignKey = ForeignKey(name = "fk_user_id"))
    open val user : User, // 멤버 하나 당 여러개의 답변이 있을 수 있다.

    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey = ForeignKey(name = "fk_question_id"))
    open val question: Question, // 질문 하나에 대한 여러개의 답변이 있을 수 있다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id", nullable = false)
    open val surveyParticipation: SurveyParticipation // 양방향 매핑
){

}
