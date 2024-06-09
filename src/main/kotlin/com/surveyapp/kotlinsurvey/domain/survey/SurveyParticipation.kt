/* SurveyParticipation.kt
* SurveyBay - 설문 참여 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.2.
*/

package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.answer.Answer
import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "survey_participations")
data class SurveyParticipation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val participationId: Long?, // 설문 참여 Id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User, // 설문 참여자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    val survey: Survey, // 설문

    @Column(name = "participation_date", nullable = false, updatable = false)
    var participationDate: LocalDate?, // 참여 일자
) {
    @PrePersist
    fun onPrePersist() {
        participationDate = LocalDate.now()
    }

    @OneToMany(
        mappedBy = "surveyParticipation",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val answers: MutableList<Answer> = mutableListOf()

}