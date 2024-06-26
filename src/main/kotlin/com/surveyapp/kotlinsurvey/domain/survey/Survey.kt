/* Survey.kt
* SurveyBay - 설문 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.2.
*/

package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.question.Question
import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "tb_survey")
class Survey(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increasement
    val surveyId: Long, // 설문 ID

    @ManyToOne
    @JoinColumn(name="user_id")
    val user : User, // 설문 작성자

    @Column
    val title : String, // 설문 제목

    @Column
    val description : String, // 설문 설명

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE) // 날짜만 입력
    val startDate : LocalDate, // 설문 시작일

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE) // 날짜만 입력
    val endDate : LocalDate, // 설문 종료일

) {
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var questions: MutableList<Question> = mutableListOf() // 해당 설문 조사의 질문 목록

    // Survey에서 SurveyParticipation으로의 일대다 매핑
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val participations: List<SurveyParticipation> = mutableListOf()

    fun findQuestion(questionId: Long?): Question? {
        return questions.find { it.questionId == questionId }
    }
}
