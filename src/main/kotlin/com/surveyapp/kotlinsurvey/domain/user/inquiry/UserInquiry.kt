package com.surveyapp.kotlinsurvey.domain.user.inquiry

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.util.Date

@Entity
class UserInquiry (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val inquiry_id : Long? = null,

    @ManyToOne
    val user: User,
    
    @Column
    val write_date : Date,

    @Column
    val content : String,

    @Column
    val answer_date : Date,

    @Column
    val reply : String,


)
{

}