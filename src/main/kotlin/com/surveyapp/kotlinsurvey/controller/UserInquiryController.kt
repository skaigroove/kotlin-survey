package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.controller.form.ReplyInquiryForm
import com.surveyapp.kotlinsurvey.controller.form.SurveyForm
import com.surveyapp.kotlinsurvey.controller.form.UserInquiryForm
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import com.surveyapp.kotlinsurvey.domain.inquiry.InquiryState
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.service.UserInquiryService
import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate
import java.time.LocalDateTime

@Controller
@RequestMapping("/home/inquiry")
class UserInquiryController(
    @Autowired private val userInquiryService: UserInquiryService,
    @Autowired private val userService: UserService,
) {
    @GetMapping("")
    fun listInquiry(model: Model): String { // 문의 게시 글 목록
        val inquiryList = userInquiryService.getInquiryList()

        model.addAttribute("inquiryList", inquiryList)

        return "inquiry" // 경로 반환 : inquiry.html
    }

    @GetMapping("/post")
    fun createInquiryForm(model: Model): String {
        // model 에 UserInquiryForm 추가
        model.addAttribute("userInquiryForm", UserInquiryForm())

        return "createInquiryForm" // 경로 반환 : createInquiryForm.html
    }

    @PostMapping("/post")
    fun writeInquiry(
        @Valid @ModelAttribute("userInquiryForm") userInquiryForm: UserInquiryForm,
        session: HttpSession,
        result: BindingResult,
        redirectAttributes: RedirectAttributes,
    ): String {

        // userInquiryFrom : binding 오류 확인
        if (result.hasErrors())
        {
            result.allErrors.forEach { error -> println("Error: ${error.defaultMessage}")}
        }

        // login 여부 확인
        val user = userService.checkLogin(session)
        if (user == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/user/login"

        // inquiry 생성
        val inquiry = createInquiry(userInquiryForm, user)

        // title 입력 x 오류 처리
        if (userInquiryForm.title.isEmpty())
            return "redirect:/home/inquiry/post?titleEmptyError=true"

        // content 입력 x 오류 처리
        if (userInquiryForm.content.isEmpty())
            return "redirect:/home/inquiry/post?contentEmptyError=true"

        userInquiryService.saveInquiry(inquiry) // 저장

        return "redirect:/home/inquiry"
    }

    fun createInquiry(inquiryForm: UserInquiryForm, user: User): UserInquiry { // inquiry 생성

        // UserInquiryForm => UserInquiry 생성자에 넣어 생성
        val inquiry = UserInquiry(null, user, inquiryForm.title, inquiryForm.writeDate, inquiryForm.content, LocalDateTime.now(), "", inquiryForm.state)

        return inquiry
    }

    @GetMapping("/{inquiryId}")
    fun detailInquiry(@PathVariable inquiryId: Long, model: Model): String{

        val inquiryPost: UserInquiry = userInquiryService.getInquiryById(inquiryId)

        model.addAttribute("inquiryPost", inquiryPost)
        model.addAttribute("replyInquiryForm", ReplyInquiryForm())

        return "inquiryPost"
    }

    @PostMapping("/{inquiryId}")
    fun replyInquiry(
        @PathVariable inquiryId: Long,
        @Valid @ModelAttribute("replyInquiryForm") replyInquiryForm: ReplyInquiryForm,
        session: HttpSession,
        result: BindingResult,
        redirectAttributes: RedirectAttributes,
    ): String {

        // userInquiryFrom : binding 오류 확인
        if (result.hasErrors())
        {
            result.allErrors.forEach { error -> println("Error: ${error.defaultMessage}")}
        }

        // login 여부 확인
        val user = userService.checkLogin(session)
        if (user == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/user/login"

        if (replyInquiryForm.reply.isEmpty())
            return "redirect:/home/inquiry/{inquiryId}?replyEmptyError=true"

        val inquiry = userInquiryService.getInquiryById(inquiryId)

        userInquiryService.saveReplyInquiry(inquiry,replyInquiryForm)

        return "redirect:redirect:/home/inquiry/{inquiryId}"

    }



}