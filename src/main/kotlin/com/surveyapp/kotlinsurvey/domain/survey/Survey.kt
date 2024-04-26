package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import java.util.*

@Entity
class Survey(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increasement
    val survey_id: Int? = null, // 설문 ID

    @OneToMany(mappedBy = "survey", cascade = [CascadeType.ALL], orphanRemoval = true)
    val questions: MutableList<Question> = mutableListOf(), // 하나의 설문 조사가 담고 있는 질문 리스트

    @ManyToOne
    @JoinColumn(name="member_id", referencedColumnName = "id")
    val author: User, // 설문 작성자

    @Column
    val title : String, // 설문 제목

    @Column
    val discription : String, // 설문 설명

    @Column
    val start_date : Date, // 설문 게시일

    @Column
    val end_date : Date, // 설문 종료일

) {

}
