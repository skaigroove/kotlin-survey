/* QuestionOption.kt
* SurveyBay - 객관식 질문 선지 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.2.
*/

package com.surveyapp.kotlinsurvey.domain.question

import com.surveyapp.kotlinsurvey.domain.answer.Answer
import jakarta.persistence.*

@Entity
@Table(name = "tb_question_option")
class QuestionOption(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val questionOptionId: Long? = null, // 선지 Id

    @Column(name = "option_index")
    val optionIndex: Int,  // 선택지의 순서를 저장하는 필드

    @Column(name = "question_option_text")
    val questionOptionText: String, // 선지 내용

    @ManyToOne
    @JoinColumn(name = "question_id")
    val question: Question // 질문

) {
    @OneToMany(mappedBy = "objectiveAnswer", fetch = FetchType.LAZY)
    val objectiveAnswers: MutableList<Answer> = mutableListOf() // 객관식 답변
}