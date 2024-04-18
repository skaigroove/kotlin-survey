package com.surveyapp.kotlinsurvey.domain.user

import com.surveyapp.kotlinsurvey.domain.user.inquiry.UserInquiry
import jakarta.persistence.*

@Entity
@Table(name = "tb_user")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increasement
    val id: Int? = null,

    @Column
    val user_id : String,

    @Column
    val user_pw : String,

    @Column
    val user_nm: String,

    @Column
    val phone : String,

    @Column
    @Enumerated(EnumType.STRING)
    val type: UserType,

    @Column
    @Enumerated(EnumType.STRING)
    val gender : GenderType,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userInquiries : MutableList<UserInquiry> = mutableListOf(),

) {
    init {
        if (user_id.isBlank()) {
            throw IllegalArgumentException("아이디는 비어 있을 수 없습니다")
        }
        if (user_pw.isBlank()) {
            throw IllegalArgumentException("비밀번호는 비어 있을 수 없습니다")
        }
        if (user_nm.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
        if (phone.isBlank()) {
            throw IllegalArgumentException("전화번호는 비어 있을 수 없습니다")
        }
    }

}