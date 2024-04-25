package com.surveyapp.kotlinsurvey.domain.survey

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.*

@Entity
class Question(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val question_id: Long? = null, // 설문 조사 질문 번호

    @ManyToOne
    @JoinColumn(name = "survey_id", referencedColumnName = "survey_id")
    val user: User, // 설문 조사 게시 글 작성자

    // class question : class Answer 를 1:n mapping
    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    val answers : MutableList<Answer> = mutableListOf(),

    @Column
    val question: String, // 설문 조사 질문
) {

}
