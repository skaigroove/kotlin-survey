package com.surveyapp.kotlinsurvey.domain.user

import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import jakarta.persistence.*

@Entity
@Table(name = "tb_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increasement
    val member_id: Int? = null, // 회원 ID - 정수

    @Column
    val user_id : String, // 사용자 ID

    @Column
    val user_pw : String, // 사용자 비밀번호

    @Column
    val user_nm: String, // 사용자 이름

    @Column
    val phone_number : String, // 사용자 전화번호

    @Column
    @Enumerated(EnumType.STRING)
    val type: UserType, // 사용자 유형 : 회원, 관리자

    @Column
    @Enumerated(EnumType.STRING)
    val gender : GenderType, // 사용자 성별

    // class User : class UserInquiry 를 1:n mapping
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userInquiries : MutableList<UserInquiry> = mutableListOf(),

    // class User : class Survey 를 1:n mapping
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val surveys : MutableList<Survey> = mutableListOf(),

    ) {
    init { // 초기화
        if (user_id.isBlank()) {
            throw IllegalArgumentException("ID 를 전달받지 못했습니다.")
        }
        if (user_pw.isBlank()) {
            throw IllegalArgumentException("비밀번호를 전달받지 못했습니다.")
        }
        if (user_nm.isBlank()) {
            throw IllegalArgumentException("이름을 전달받지 못했습니다.")
        }
        if (phone_number.isBlank()) {
            throw IllegalArgumentException("전화번호를 전달받지 못했습니다.")
        }
    }

}


