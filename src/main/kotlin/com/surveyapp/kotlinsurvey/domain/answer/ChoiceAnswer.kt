package com.surveyapp.kotlinsurvey.domain.answer

import com.surveyapp.kotlinsurvey.domain.question.Question
import com.surveyapp.kotlinsurvey.domain.question.QuestionOption
import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
@Table(name = "tb_choice_answer")
class ChoiceAnswer(

    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val answerId: Long?, // 객관식 질문 번호

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
    override val answerType: AnswerType = AnswerType.MULTIPLE_CHOICE, // 답변 유형

    @Column
    override val text : String? = null, // 주관식 답변 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    val selectedOption: QuestionOption?

    ) : Answer(answerId, AnswerType.MULTIPLE_CHOICE, user, question, text, surveyParticipation) {
}
