package com.surveyapp.kotlinsurvey.service.user

import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException


@Service
@Transactional
class UserService(@Autowired private val userRepository: UserRepository) {

    /* userRepository에 받은 user 정보로 회원가입*/
    @Transactional
    fun join(user: User) {
        validateDuplicateUser(user) // 중복이면 에러를 반환한다
        userRepository.save(user)
        return
    }

    /* loginId를 기준으로 저장소의 유저가 중복되는지 검사*/
    fun validateDuplicateUser(user: User) {
        val findMember: User? = userRepository.findByLoginId(user.loginId)
        if (findMember != null) // 레포지토리에 멤버가 존재한다면
            throw IllegalArgumentException("이미 존재하는 회원입니다.")
    }

    /* 회원 전체 조회*/
    fun findUsers(): List<User> { return userRepository.findAll() }

    /* userId(Long)으로 회원 한 명 조회*/
    fun findOne(userId : Long) : User { return userRepository.findOne(userId)}

}