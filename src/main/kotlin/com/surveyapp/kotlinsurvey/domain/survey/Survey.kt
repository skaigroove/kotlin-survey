package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "tb_survey")
class Survey(

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increasement
    val survey_id: Long? = null, // 설문 ID

    @ManyToOne
    @NotNull
    @JoinColumn(name="user_id")
    val user : User, // 설문 작성자

    @Column
    val title : String, // 설문 제목

    @Column
    val discription : String, // 설문 설명

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE) // 날짜만 입력
    val startDate : LocalDate, // 설문 게시일

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE) // 날짜만 입력
    val endDate : LocalDate, // 설문 종료일

) {
    @OneToMany(mappedBy = "survey", cascade = [CascadeType.ALL], orphanRemoval = true)
    val questions: MutableList<Question>? = null // 하나의 설문 조사가 담고 있는 질문 리스트

    // Survey에서 SurveyParticipation으로의 일대다 매핑
    @OneToMany(mappedBy = "survey", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val participations: List<SurveyParticipation> = mutableListOf()
}
