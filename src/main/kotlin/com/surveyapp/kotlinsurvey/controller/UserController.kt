package com.surveyapp.kotlinsurvey.controller

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
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDate

@RequestMapping("/user") // endpoint
@Controller
class UserController(
        @Autowired private val userService: UserService,
) {
    @GetMapping("/new")
    fun createForm(model: Model): String {
        model.addAttribute("userForm", UserForm("", "", "", LocalDate.now(), GenderType.FEMALE, "")) // 기본값으로 초기화
        return "createUserForm"
    }

    @PostMapping("/new")
    fun createUser(@Valid userForm: UserForm, result: BindingResult): String{

        if (result.hasErrors())
            return "createUserForm"

        val user = User(null, userForm.loginId, userForm.password, userForm.name,userForm.birthDate,userForm.genderType,userForm.phoneNumber,UserType.CLIENT)
        userService.join(user)
        return "welcome"
    }

    @GetMapping("/login")
    fun loginForm(model :Model): String {
        model.addAttribute("loginForm",LoginForm("","")) // 초기화한 폼을 모델에 추가
        return "login"
    }

    @PostMapping("/login")
    fun login(@ModelAttribute loginForm: LoginForm, session: HttpSession) : String{
        val loginResult : LoginForm? = userService.login(loginForm)
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginId", loginResult.loginId)
            println("로그인에 성공하였습니다")
            return "redirect:/home/list"
        } else {
            // login 실패
            println("로그인에 실패하였습니다")
            return "login"
        }

    }
}