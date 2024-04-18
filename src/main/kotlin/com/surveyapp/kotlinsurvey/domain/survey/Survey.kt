package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.util.*

@Entity
class Survey(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increasement
    val survey_id: Int? = null, // 설문 조사 게시 글 ID

    @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "id")
    val user: User, // 설문 조사 게시 글 작성자

    @Column
    val title : String, // 설문 조사 게시 글 제목

    @Column
    val discription : String, // 설문 조사 게시 글 내용

    @Column
    val start_date : Date, // 설문 조사 시작일

    @Column
    val end_date : Date, // 설문 조사 종료일

) {

}
