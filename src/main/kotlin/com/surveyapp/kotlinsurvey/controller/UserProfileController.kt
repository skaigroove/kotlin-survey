package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.controller.form.LoginForm
import com.surveyapp.kotlinsurvey.controller.form.UserForm
import com.surveyapp.kotlinsurvey.domain.user.GenderType
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.repository.UserRepository
import org.springframework.ui.Model
import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Controller
@RequestMapping("/user")
class UserProfileController(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository
) {

    @GetMapping("/profile")
    fun userProfile(session: HttpSession, model: Model): String {

        // session으로부터 loginId를 받아와, 해당 유저를 찾는다.
        val sessionLoginId = session.getAttribute("loginId") as String
        val user = userService.findUserByLoginId(sessionLoginId)

        // 패스워드 가공
        val maskedPassword = if (user!!.password.length > 2) {
            user.password.take(2) + "*".repeat(user.password.length - 2)
        } else {
            user.password
        }

        val userCopy = User(
            userId = user.userId,
            loginId = user.loginId,
            password = maskedPassword, // 가공된 패스워드 사용
            name = user.name,
            birthDate = user.birthDate,
            genderType = user.genderType,
            phoneNumber = user.phoneNumber,
            userType = user.userType
        )

        model.addAttribute("loginUser", userCopy)

        return "userProfile"
    }

    @GetMapping("/profile/edit")
    fun editForm(model: Model, session: HttpSession):String{

        // session으로부터 loginId를 받아와, 해당 유저를 찾는다.
        val sessionLoginId = session.getAttribute("loginId") as String
        val user : User? = userService.findUserByLoginId(sessionLoginId)

        // 찾은 유저의 값들을 userForm에 삽입한다.
        model.addAttribute("userForm", UserForm(user!!.loginId, user.password, user.name, user.birthDate, user.genderType, user.phoneNumber)) // 초기화한 폼을 모델에 추가
        model.addAttribute("loginUser",user)

        return "editProfile"
    }

    @PostMapping("/profile/edit")
    fun editUserProfile(
        @Valid @ModelAttribute("user") userForm: UserForm,
        result: BindingResult,
        session: HttpSession,
    ): String {
        if (result.hasErrors()) {
            return "userProfile"
        }

        val sessionUser = userService.findUserByLoginId(session.getAttribute("loginId") as String)
        val formUser = userRepository.findByPhoneNumber(userForm.phoneNumber)

        if (formUser != null && userService.validateDuplicateUserByPhoneNum(formUser) && userForm.phoneNumber != sessionUser?.phoneNumber) {
            return "redirect:/user/profile/edit?phoneNumberDuplicatedError=true"
        }

        val loginId = session.getAttribute("loginId") as String
        val user = userService.updateUser(loginId, userForm)

        return "redirect:/user/profile"
    }


}