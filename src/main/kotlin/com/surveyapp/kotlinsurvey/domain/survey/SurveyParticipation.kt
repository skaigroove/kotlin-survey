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
    val participationId: Long?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    val survey: Survey,

    @Column(name = "participation_date", nullable = false, updatable = false)
    var participationDate: LocalDate?,
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