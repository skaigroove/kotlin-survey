package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    @Query("select u from User u where u.loginId = :loginId")
    fun findByLoginId(@Param("loginId") loginId: String): User?

    @Query("select u from User u where u.phoneNumber = :phoneNumber")
    fun findByPhoneNumber(@Param("phoneNumber") phoneNumber: String): User?
}
