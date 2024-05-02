package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.user.GenderType
import java.time.LocalDate

data class UserForm(
    val loginId : String,
    val password : String,
    val name : String,
    val birthDate : LocalDate,
    val genderType : GenderType,
    val phoneNumber: String,
    // userId와 userType은 제외
)