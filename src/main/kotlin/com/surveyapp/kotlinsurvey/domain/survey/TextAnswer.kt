package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
@Table(name = "tb_text_answer")
class TextAnswer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val answerId: Long? = null, // 객관식 질문 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "fk_user_id"))
    override val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = ForeignKey(name = "fk_question_id"))
    override val question: Question,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id", nullable = false)
    override val surveyParticipation: SurveyParticipation, // 양방향 매핑

    val text: String // 주관식 답변 내용

) : Answer(answerId, user, question, surveyParticipation)
{

}
