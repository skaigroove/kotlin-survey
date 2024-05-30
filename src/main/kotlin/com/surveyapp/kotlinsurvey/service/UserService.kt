package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.controller.form.LoginForm
import com.surveyapp.kotlinsurvey.controller.form.UserForm
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.domain.user.UserType
import com.surveyapp.kotlinsurvey.repository.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional
    fun saveUser(user: User) {
        userRepository.save(user)
    }

    fun findUserById(userId: Long): User? {
        return userRepository.findById(userId).orElse(null)
    }

    fun findAllUsers(): List<User>? {
        return userRepository.findAll()
    }

    fun findUserByLoginId(loginId: String): User? {
        return userRepository.findByLoginId(loginId)
    }

    fun findUserByPhoneNumber(phoneNumber: String): User? {
        return userRepository.findByPhoneNumber(phoneNumber)
    }

    fun checkLogin(session: HttpSession): User? {
        val loginId = session.getAttribute("loginId") as? String // 사용자 - loginId get
        if (loginId == null) { // 로그인 안 된 상황
            println("사용자 로그인 정보가 없습니다.")
            return null
        } else { // 로그인 된 상황
            val user = userRepository.findByLoginId(loginId)
            return user
        }
    }

    fun validateDuplicateUserByPhoneNum(user: User) : Boolean{
        val findMember: User? = userRepository.findByPhoneNumber(user.phoneNumber)
        if (findMember != null) // 레포지토리에 멤버가 존재한다면
            return true
        return false
    }

    /* loginId를 기준으로 저장소의 유저가 중복되는지 검사*/
    fun validateDuplicateUserByLoginId(user: User) : Boolean{
        val findMember: User? = userRepository.findByLoginId(user.loginId)
        if (findMember != null) // 레포지토리에 멤버가 존재한다면
            return true
        return false
    }

    /* 로그인*/
    fun login(loginForm: LoginForm): LoginForm? {
        /*
             1. 회원이 입력한 loginId로 DB에서 조회를 함
             2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
        */

        val byloginId = userRepository.findByLoginId(loginForm.loginId) // 유저가 입력한 loginId로 부터 User객체를 받아옴
        if (byloginId != null) {
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            if (byloginId.password.equals(loginForm.password)) { // 유저가 입력한 password와 Repo에 저장되어있는 password가 일치한다면
                return loginForm
            } else {
                // 비밀번호 불일치(로그인실패)
                return null
            }
        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null
        }
    }

    /* userRepository에 받은 user 정보로 회원가입*/
    @Transactional
    fun join(user: User): User {
        validateDuplicateUserByLoginId(user) // 중복이면 에러를 반환한다
        userRepository.save(user)
        return user
    }

    fun validateAdmin(user: User): Boolean {

        if (user.userType == UserType.ADMIN) {
            return true
        }
        return false
    }

    fun updateUser(loginId: String, userForm: UserForm) {
        val user = userRepository.findByLoginId(loginId)

        user?.let {
            it.name = userForm.name
            it.password = userForm.password
            it.birthDate = userForm.birthDate
            it.genderType = userForm.genderType
            it.phoneNumber = userForm.phoneNumber
            userRepository.save(it)
        }
    }


}
