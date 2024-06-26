/* User.kt
* SurveyBay - 사용자 (관리자, 회원) 관련 doamin
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.5.4.
*/

package com.surveyapp.kotlinsurvey.domain.user

import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "tb_user",
    uniqueConstraints = [UniqueConstraint(name = "uk_member_login_id", columnNames = ["loginId"])]
)
class User(

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increasement
    val userId: Long? = null, // 회원 ID - 정수

    @Column(name = "login_id", nullable = false, length = 20, updatable = false)
    val loginId: String, // 사용자 ID

    @Column(name = "password", nullable = false, length = 20)
    var password: String, // 사용자 비밀번호

    @Column(name = "name", nullable = false, length = 10)
    var name: String, // 사용자 이름

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE) // 날짜만 입력
    var birthDate: LocalDate, // 사용자 생년월일

    @Column(name = "gender_type")
    @Enumerated(EnumType.STRING)
    var genderType: GenderType, // 사용자 성별

    @Column(name = "phone_number", nullable = false, length = 11)
    var phoneNumber: String, // 사용자 전화번호

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    val userType: UserType? // 사용자 권한은 nullable로 설정. 처음부터 값을 받지 않고, userService 단에서 회원 가입 시 default 값으로 'USER' 설정.

) {

    // class User : class UserInquiry 를 1:n mapping
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userInquiries : MutableList<UserInquiry>? = null

    // class User : class Survey 를 1:n mapping
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val surveys: MutableList<Survey>? = null

    // User에서 SurveyParticipation으로의 일대다 매핑
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val participations: List<SurveyParticipation> = mutableListOf()
}


