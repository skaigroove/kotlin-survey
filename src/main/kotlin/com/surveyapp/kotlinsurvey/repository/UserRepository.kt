package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User,Long>{
    fun findByLoginId(loginId:String): User? // 회원가입 시 동일 아이디 체크 위함
}
