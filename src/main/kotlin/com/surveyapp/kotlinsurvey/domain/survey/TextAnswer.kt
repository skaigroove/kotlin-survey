package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.lang.reflect.Member

@Entity
class TextAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val answer_id: Long? = null, // 객관식 질문 번호

    @ManyToOne
    @JoinColumn(name = "member_id")
    override val join_user: User,

    @ManyToOne
    @JoinColumn(name = "question_id")
    override val question: Question,

    val text: String // 주관식 답변 내용

) : Answer(answer_id, join_user, question)
{

}
