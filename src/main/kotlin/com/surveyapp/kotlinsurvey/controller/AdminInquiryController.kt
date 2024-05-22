package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.service.UserInquiryService
import com.surveyapp.kotlinsurvey.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class AdminInquiryController(
    @Autowired private val userInquiryService: UserInquiryService
)
{
    @GetMapping("/inquiry")
    fun getInquiries(model: Model): String {
        val adminInquiryList = userInquiryService.getInquiryList()
        model.addAttribute("adminInquiryList", adminInquiryList)
        return "adminInquiry/adminInquiryList" // adminInquiryList.html 템플릿 반환
    }

    @GetMapping("/inquiry/{inquiryId}")
    fun getInquiryDetail(@PathVariable inquiryId: Long, model: Model): String {
        val inquiry = userInquiryService.getInquiryById(inquiryId)
        model.addAttribute("adminInquiryPost", inquiry)
        return "adminInquiry/adminInquiryDetail" // adminInquiryDetail.html 템플릿 반환
    }
}

