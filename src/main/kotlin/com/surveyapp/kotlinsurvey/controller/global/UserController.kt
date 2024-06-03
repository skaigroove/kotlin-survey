package com.surveyapp.kotlinsurvey.controller.global

import com.surveyapp.kotlinsurvey.controller.form.LoginForm
import com.surveyapp.kotlinsurvey.controller.form.UserForm
import com.surveyapp.kotlinsurvey.domain.user.GenderType
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.domain.user.UserType
import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import java.time.LocalDate

@Controller
class UserController(
    @Autowired private val userService: UserService,
) {
    @GetMapping("/user/new")
    fun createForm(model: Model): String {
        model.addAttribute("userForm", UserForm("", "", "", LocalDate.now(), GenderType.FEMALE, "")) // 기본값으로 초기화
        return "sign-up"
    }

    @PostMapping("/user/new")
    fun createUser(@Valid userForm: UserForm, result: BindingResult): String {

        if (result.hasErrors())
            return "sign-up"

        val user = User(
            null,
            userForm.loginId,
            userForm.password,
            userForm.name,
            userForm.birthDate,
            userForm.genderType,
            userForm.phoneNumber,
            UserType.CLIENT
        )

        // 레포지토리에 이미 유저가 존재한다면
        if (userService.validateDuplicateUserByLoginId(user))
            return "redirect:/user/new?loginIdDuplicatedError=true"

        if (userService.validateDuplicateUserByPhoneNum(user))
            return "redirect:/user/new?phoneNumberDuplicatedError=true"

        // 유저가 존재하지 않는다면 회원가입하고 웰컴 페이지로 이동
        userService.join(user)
        return "redirect:/"
    }

    @GetMapping("/")
    fun loginForm(model: Model): String {
        model.addAttribute("loginForm", LoginForm("", "")) // 초기화한 폼을 모델에 추가
        return "sign-in"
    }

    @PostMapping("/")
    fun login(@ModelAttribute loginForm: LoginForm, session: HttpSession, model: Model): String {
        val loginResult: LoginForm? = userService.login(loginForm)

        if (loginResult != null) {
            // login 성공
            val loginUserName: String = userService.findUserByLoginId(loginForm.loginId)!!.name
            model.addAttribute("username", loginUserName)
            session.setAttribute("loginId", loginResult.loginId)
            session.setAttribute("username", loginUserName)

            //로그인 유저 이름 출력
            println("username: $loginUserName")
            println("로그인에 성공하였습니다")

            val user = userService.findUserByLoginId(loginForm.loginId)

            return when (user?.userType) {
                UserType.ADMIN -> "admin-auth/home-admin"
                UserType.CLIENT -> "user-auth/home-user"
                else -> "redirect:/"
            }

        } else {
            // login 실패
            println("로그인에 실패하였습니다")
            return "redirect:/?error=true"
        }
    }
}