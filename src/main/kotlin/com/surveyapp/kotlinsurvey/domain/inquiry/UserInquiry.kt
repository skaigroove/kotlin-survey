package com.surveyapp.kotlinsurvey.domain.inquiry

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

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
    val title: String, // 문의 게시 글 제목

    @Column(name = "write_date", nullable = false, updatable = false)
    val writeDate : LocalDateTime? = null, // 문의 작성 일자

    @Column
    val content : String, // 문의 내용

    @Column(name = "answer_date", nullable = false, updatable = true)
    var answerDate : LocalDateTime? = null, // 답변 작성 일자

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
