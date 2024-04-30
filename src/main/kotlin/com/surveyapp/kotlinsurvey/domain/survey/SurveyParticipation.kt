package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "survey_participations")
data class SurveyParticipation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val participationId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    val survey: Survey,

    @Column(name = "participation_date", nullable = false, updatable = false)
    var participationDate: Date? = null
) {
    @PrePersist
    fun onPrePersist() {
        participationDate = Date()
    }

    @OneToMany(mappedBy = "surveyParticipation", cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers: MutableList<Answer> = mutableListOf()
}