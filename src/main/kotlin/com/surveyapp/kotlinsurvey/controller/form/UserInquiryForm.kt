package com.surveyapp.kotlinsurvey.controller.form

import com.surveyapp.kotlinsurvey.domain.inquiry.InquiryState
import java.time.LocalDateTime

data class UserInquiryForm(
    var state: InquiryState = InquiryState.UNCOMPLETE,
    var title: String = "",
    var writeDate: LocalDateTime = LocalDateTime.now(),
    var content: String = "",
    )
