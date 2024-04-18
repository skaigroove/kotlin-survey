package com.surveyapp.kotlinsurvey.repository.member

import com.surveyapp.kotlinsurvey.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<User, Int> { // member 객체와 id 값으로 repository에 저장됨

    fun findByName(bookName: String): User?
}