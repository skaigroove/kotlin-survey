package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.controller.form.ReplyInquiryForm
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
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

@Controller
@RequestMapping("/admin")
class AdminInquiryController(
    @Autowired private val userInquiryService: UserInquiryService,
    @Autowired private val userService: UserService,
)
{
    @GetMapping("/inquiry")
    fun adminInquiryList(model: Model): String { // 관리자 (admin) - 문의 목록 관련 처리
        val inquiryList = userInquiryService.getInquiryList() // 모든 문의 글 get

        model.addAttribute("adminInquiryList", inquiryList) // 속성 추가

        return "adminInquiry/adminInquiryList";
    }

    @GetMapping("/inquiry/{inquiryId}")
    fun adminDetailInquiry(@PathVariable inquiryId: Long, model: Model): String{ // 관리자 (admin) - 문의 글 상세 보기 관련 처리
        val inquiryPost: UserInquiry = userInquiryService.getInquiryById(inquiryId)

        model.addAttribute("adminInquiryPost", inquiryPost)
        model.addAttribute("replyInquiryForm", ReplyInquiryForm()) // 답변 양식

        return "adminInquiry/adminInquiryDetail";
    }

    @PostMapping("/inquiry/{inquiryId}")
    fun replyInquiry(
        @PathVariable inquiryId: Long,
            @Valid @ModelAttribute("replyInquiryForm") replyInquiryForm: ReplyInquiryForm,
            session: HttpSession,
            result: BindingResult,
            redirectAttributes: RedirectAttributes,
        ): String { // 관리자 (admin) - 문의 글 답변 관련 처리 : 처음 답변 작성할 때

            // replyInquiryForm : binding 오류 확인
            if (result.hasErrors())
            {
                result.allErrors.forEach { error -> println("Error: ${error.defaultMessage}")}
            }

            // login 여부 확인
            val user = userService.checkLogin(session)
            if (user == null) // 로그인 안 되었음 => null 반환됨
                return "redirect:/"
            /*
            if (replyInquiryForm.reply.isEmpty())
                return "redirect:/home/inquiry/{inquiryId}?replyEmptyError=true"
            */

            val inquiry = userInquiryService.getInquiryById(inquiryId)

            userInquiryService.saveReplyInquiry(inquiry,replyInquiryForm)

            return "redirect:/admin/inquiry/{inquiryId}"
    }

    @PostMapping("/inquiry/edit/{inquiryId}")
    fun editReply(
        @PathVariable inquiryId: Long,
        @RequestParam("replyEdit") reply: String,
        session: HttpSession,
        redirectAttributes: RedirectAttributes
    ): String { // 관리자 (admin) - 문의 답변 수정 처리 함수
        // login 여부 확인
        val user = userService.checkLogin(session)
        if (user == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        userInquiryService.editReplyInquiry(inquiryId, reply)
        redirectAttributes.addFlashAttribute("message", "답변이 수정되었습니다.")

        return "redirect:/admin/inquiry/{inquiryId}"
    }
}

