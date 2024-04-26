package com.surveyapp.kotlinsurvey.repository.user

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    @PersistenceContext // 생성자 주입 자동화 어노테이션
    private val em : EntityManager // 생성자 주입
){
    fun save(user : User){
    }
}
