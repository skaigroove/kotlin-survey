package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
@Table(name = "tb_choice_answer")
class ChoiceAnswer(

    @Id
    @Column(name="answer_id")
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

    @Column
    val selectedOptionId: Long // 선택한 옵션을 저장하는 필드

    ) : Answer(answerId, user, question,surveyParticipation)
    {
}
