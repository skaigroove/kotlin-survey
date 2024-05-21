package com.surveyapp.kotlinsurvey.controller.form

import java.time.LocalDateTime

data class ReplyInquiryForm(
    var answerDate: LocalDateTime = LocalDateTime.now(),
    var reply: String = ""
)
