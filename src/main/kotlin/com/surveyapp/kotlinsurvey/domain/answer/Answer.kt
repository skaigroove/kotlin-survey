package com.surveyapp.kotlinsurvey.domain.answer

import com.surveyapp.kotlinsurvey.domain.question.Question
import com.surveyapp.kotlinsurvey.domain.question.QuestionOption
import com.surveyapp.kotlinsurvey.domain.survey.Survey
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

    @Column(name = "answer_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    open val answerType: AnswerType, // 답변 유형

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false , foreignKey = ForeignKey(name = "fk_user_id"))
    open val user: User, // 멤버 하나당 여러개의 답변이 있을 수 있다.

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false , foreignKey = ForeignKey(name = "fk_question_id"))
    open val question: Question, // 질문 하나당 여러 개의 답변이 있을 수 있다.

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participation_id", nullable = false)
    open val surveyParticipation: SurveyParticipation, // 양방향 매핑
) {
    val textAnswer: String?
        get() = (this as? TextAnswer)?.text

    val selectedOptionText: String?
        get() = (this as? ChoiceAnswer)?.selectedOption?.questionOptionText

    // 선택된 답변
    val choiceAnswer: QuestionOption
        get() = (this as? ChoiceAnswer)?.selectedOption
            ?: throw IllegalStateException("This answer is not a choice answer")

    fun getAnswer(type : AnswerType) : String?
    {
        if(type == AnswerType.SUBJECTIVE)
        {
            return textAnswer
        }
        else if(type == AnswerType.MULTIPLE_CHOICE)
        {
            return selectedOptionText
        }
        else
        {
            return null
        }
    }
}
