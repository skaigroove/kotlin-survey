/* UserService.kt
* SurveyBay - 사용자 관련 서비스 클래스
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.2. 회원 탈퇴 (회원 정보 삭제) 함수 작성
*/


package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.controller.form.LoginForm
import com.surveyapp.kotlinsurvey.controller.form.UserForm
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.domain.user.UserType
import com.surveyapp.kotlinsurvey.repository.UserRepository
import jakarta.servlet.http.HttpSession
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
@Transactional
class UserService(@Autowired private val userRepository: UserRepository) {

    /* userRepository에 받은 user 정보로 회원 가입*/
    @Transactional
    fun join(user: User): User {
        validateDuplicateUserByLoginId(user) // 중복이면 에러를 반환한다
        userRepository.save(user)
        return user
    }

    fun validateAdmin(user: User): Boolean { // 관리자 계정인지 아닌지 판단

        if (user.userType == UserType.ADMIN) {
            return true
        }
        return false
    }


    /* loginId를 기준으로 저장소의 유저가 중복되는지 검사*/
    fun validateDuplicateUserByLoginId(user: User) : Boolean{
        val findMember: User? = userRepository.findByLoginId(user.loginId)
        if (findMember != null) // 레포지토리에 멤버가 존재한다면
            return true
        return false
    }

    fun validateDuplicateUserByPhoneNum(user: User) : Boolean{ // 전화번호 중복 확인
        val findMember: User? = userRepository.findByPhoneNumber(user.phoneNumber)
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

        val byloginId = userRepository.findByLoginId(loginForm.loginId) // 유저가 입력한 loginId로 부터 User 객체를 받아 옴
        if (byloginId != null) {
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            if (byloginId.password.equals(loginForm.password)) { // 유저가 입력한 password 와 Repo에 저장되어 있는 password 가 일치한다면
                return loginForm
            } else {
                // 비밀번호 불일치(로그인 실패)
                return null
            }
        } else {
            // 조회 결과가 없다 (해당 이메일을 가진 회원이 없다)
            return null
        }
    }

    fun checkLogin(session: HttpSession): User? { // 로그인 상태인지 확인
        val loginId = session.getAttribute("loginId") as? String // 사용자 - loginId get
        if (loginId == null) { // 로그인 안 된 상황
            println("사용자 로그인 정보가 없습니다.")

            return null
        }
        else { // 로그인 된 상황
            return userRepository.findByLoginId(loginId) // user 반환
        }
    }

    /* userId(Long)으로 user 한 명 조회 */
    fun findOne(userId: Long): User {
        return userRepository.findOne(userId)
    }

    fun findUserByLoginId(loginId:String):User?{ // loginId 에 해당하는 user 반환
        return userRepository.findByLoginId(loginId)
    }

    fun updateUser(loginId: String, userForm: UserForm) { // 수정된 회원 정보 수정
        val user = userRepository.findByLoginId(loginId)

        user?.let {
            it.name = userForm.name // 이름
            it.password = userForm.password // 비밀번호
            it.birthDate = userForm.birthDate // 생년월일
            it.genderType = userForm.genderType // 성별
            it.phoneNumber = userForm.phoneNumber // 전화번호
            userRepository.save(it)
        }
    }

    fun getUserList(): List<User>? { // 모든 사용자 (관리자, 회원) 목록을 List 로 반환
        return userRepository.findAll()
    }

    fun deleteUserByUserId(userId: Long) { // userId 에 해당하는 user 정보 삭제
        val user = userRepository.findOne(userId)

        userRepository.deleteUser(user)
    }

}