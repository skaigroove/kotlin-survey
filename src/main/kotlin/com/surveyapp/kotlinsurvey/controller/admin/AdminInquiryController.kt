package com.surveyapp.kotlinsurvey.controller.admin

import com.surveyapp.kotlinsurvey.controller.form.ReplyInquiryForm
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import com.surveyapp.kotlinsurvey.service.UserInquiryService
import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

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

        return "admin-auth/admin-inquiry/admin-inquiry-list";
    }

    @GetMapping("/inquiry/{inquiryId}")
    fun adminDetailInquiry(@PathVariable inquiryId: Long, model: Model): String{ // 관리자 (admin) - 문의 글 상세 보기 관련 처리
        val inquiryPost: UserInquiry = userInquiryService.getInquiryById(inquiryId)

        model.addAttribute("adminInquiryPost", inquiryPost)
        model.addAttribute("replyInquiryForm", ReplyInquiryForm()) // 답변 양식

        return "admin-auth/admin-inquiry/admin-inquiry-detail";
    }

    @PostMapping("/inquiry/{inquiryId}")
    @ResponseBody
    fun replyInquiry(
        @PathVariable inquiryId: Long,
        @RequestParam("reply") reply: String,
        session: HttpSession
    ): ResponseEntity<String> { // 관리자 (admin) - 문의 글 답변 관련 처리 : 처음 답변 작성할 때
        // login 여부 확인
        val user = userService.checkLogin(session)
        if (user == null) // 로그인 안 되었음 => null 반환됨
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")

        val inquiry = userInquiryService.getInquiryById(inquiryId)
        userInquiryService.saveReplyInquiry(inquiry, reply)

        return ResponseEntity.ok("Reply saved successfully")
    }

    @PostMapping("/inquiry/edit/{inquiryId}")
    @ResponseBody
    fun editReply(
        @PathVariable inquiryId: Long,
        @RequestParam("replyEdit") reply: String,
        session: HttpSession
    ): ResponseEntity<String> { // 관리자 (admin) - 문의 답변 수정 처리 함수
        // login 여부 확인
        val user = userService.checkLogin(session)
        if (user == null) // 로그인 안 되었음 => null 반환됨
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")

        if (reply.isEmpty()) {
            return ResponseEntity.badRequest().body("Reply cannot be empty")
        }

        userInquiryService.editReplyInquiry(inquiryId, reply)
        return ResponseEntity.ok("Reply edited successfully")
    }
}

