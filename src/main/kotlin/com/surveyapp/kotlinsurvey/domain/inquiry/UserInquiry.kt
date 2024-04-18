package com.surveyapp.kotlinsurvey.domain.inquiry

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.util.Date

@Entity
class UserInquiry (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val inquiry_id : Long? = null,

    @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "id")
    val user: User,

    @Column
    val write_date : Date,

    @Column
    val content : String,

    @Column
    val answer_date : Date,

    @Column
    val reply : String,

    @Column
    var status : InquiryState = InquiryState.UNCOMPLETE // default value
)
{
    val isComplete: Boolean
        get() = this.status == InquiryState.COMPLETE // complete 되었으면 true 반환
    fun CompleteInquiry(){
        this.status = InquiryState.COMPLETE
    }
}