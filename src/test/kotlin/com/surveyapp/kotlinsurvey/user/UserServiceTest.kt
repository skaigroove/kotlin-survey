package com.surveyapp.kotlinsurvey.user

import com.surveyapp.kotlinsurvey.service.UserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTest (
   @Autowired
   private val userService : UserService
){
    @Test
    @DisplayName("회원을 레포지토리에 넣는 테스트")
    fun join(){
//        //given
//        val userDtoRequest = UserDtoRequest(
//            userId = null,
//            loginId = "hgd@gmail.com",
//            password = "1234",
//            name = "홍길동",
//            birthDate = LocalDate.of(1990, 1, 1),
//            gender = GenderType.MALE,
//            type = UserType.CLIENT,
//            phoneNumber = "01012345678")
//        //when
//        userService.signUp(userDtoRequest)
    }
}