package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.domain.user.GenderType
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.domain.user.UserType
import com.surveyapp.kotlinsurvey.service.user.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RequestMapping("/user") // endpoint
@RestController
class UserController(
    @Autowired private val userService: UserService,
) {
    @GetMapping("/new")
    fun createForm(model: Model): String {
        model.addAttribute("userForm", UserForm("", "", "", LocalDate.now(), GenderType.FEMALE, "")) // 기본값으로 초기화
        return "createUserForm"
    }

    @PostMapping("/new")
    fun crateUser(@Valid userForm: UserForm, result: BindingResult): String {

        if (result.hasErrors())
            return "createUserForm"
        val user = User(null, userForm.loginId, userForm.password, userForm.name,userForm.birthDate,userForm.genderType,userForm.phoneNumber,
            UserType.CLIENT)
        userService.join(user)
        return "/"
    }
}