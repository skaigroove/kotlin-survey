package com.surveyapp.kotlinsurvey.domain.answer

import com.surveyapp.kotlinsurvey.domain.question.Question
import com.surveyapp.kotlinsurvey.domain.question.QuestionOption
import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
@Table(name = "tb_answer")
class Answer(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val answerId: Long? = null, // 설문 내 질문 번호

    @Column(name = "answer_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val answerType: AnswerType, // 답변 유형

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "fk_user_id"))
    val user: User, // 멤버 하나당 여러개의 답변이 있을 수 있다.

    @ManyToOne
    @JoinColumn(name = "question_id", foreignKey = ForeignKey(name = "fk_question_id"))
    val question: Question, // 질문 하나당 여러 개의 답변이 있을 수 있다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id", nullable = false)
    val surveyParticipation: SurveyParticipation, // 양방향 매핑

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "objectiveAnswers")
    var objectiveAnswer: QuestionOption? = null, // 주관식 답변일 때, 선택된 선택지

    @Column
    val subjectiveAnswer: String? = null // 주관식 답변 내용
){
}
