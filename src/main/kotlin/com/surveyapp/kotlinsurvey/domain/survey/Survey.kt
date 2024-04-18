package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.util.*

@Entity
class Survey(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increasement
    val survey_id: Int? = null,

    @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "id")
    val user: User,

    @Column
    val title : String,

    @Column
    val discription : String,

    @Column
    val start_date : Date,

    @Column
    val end_date : Date,

) {


}