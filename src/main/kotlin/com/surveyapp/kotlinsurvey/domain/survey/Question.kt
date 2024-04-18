package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
class Question(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val question_id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "survey_id", referencedColumnName = "survey_id")
    val user: User,

    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers : MutableList<Answer> = mutableListOf(),

    @Column
    val question: String,
) {
}