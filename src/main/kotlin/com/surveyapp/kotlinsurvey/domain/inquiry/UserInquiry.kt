package com.surveyapp.kotlinsurvey.domain.inquiry

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.util.Date

@Entity
class UserInquiry (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    val id : Long? = null, // 문의 글 번호

    @ManyToOne
    @JoinColumn(name="member_id")
    val user: User, // 문의 작성자

    @Column
    val write_date : Date, // 문의 작성 일자

    @Column
    val content : String, // 문의 내용

    @Column
    val answer_date : Date, // 답변 작성 일자

    @Column
    val reply : String, // 답변

    @Column
    @Enumerated(EnumType.STRING)
    var status : InquiryState = InquiryState.UNCOMPLETE // default value
)
{
    val isComplete: Boolean
        get() = this.status == InquiryState.COMPLETE // complete 됨 => true 반환
    fun CompleteInquiry(){
        this.status = InquiryState.COMPLETE
    }
}
