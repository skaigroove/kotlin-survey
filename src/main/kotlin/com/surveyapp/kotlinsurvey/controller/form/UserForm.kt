package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.user.GenderType
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class UserForm(
    @NotBlank val loginId : String,
    @NotBlank val password : String,
    @NotBlank val name : String,
    @NotBlank val birthDate : LocalDate,
    @NotBlank val genderType : GenderType,
    @NotBlank val phoneNumber: String,
    // userId와 userType은 제외
)