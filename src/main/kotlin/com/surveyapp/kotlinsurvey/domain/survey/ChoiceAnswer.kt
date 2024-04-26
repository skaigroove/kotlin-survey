package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
class ChoiceAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val answer_id: Long? = null, // 객관식 질문 번호

    @ManyToOne
    @JoinColumn(name = "member_id")
    override val join_user: User,

    @ManyToOne
    @JoinColumn(name = "question_id")
    override val question: Question,

    @ManyToOne
    @JoinColumn(name = "choice_id")
    val choice: Choice // 선택된 객관식 답변

    ) : Answer(answer_id, join_user, question)
    {
}