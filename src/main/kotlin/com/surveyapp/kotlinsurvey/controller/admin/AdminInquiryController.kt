/* AdminInquiryController.kt
* SurveyBay - 관리자 : 1:1 문의 관리 관련 Controller
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.6. 문의 답변 작성, 수정 시 경로 처리 해결
*/

package com.surveyapp.kotlinsurvey.controller.admin

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

        return "admin-auth/admin-inquiry/admin-inquiry-list"; // 경로 반환
    }

    @GetMapping("/inquiry/{inquiryId}")
    fun adminDetailInquiry(@PathVariable inquiryId: Long, model: Model): String{ // 관리자 (admin) - 문의 글 상세 보기 관련 처리
        val inquiryPost: UserInquiry = userInquiryService.getInquiryById(inquiryId)

        model.addAttribute("adminInquiryPost", inquiryPost) // 속성 추가

        return "admin-auth/admin-inquiry/admin-inquiry-detail"; // 경로 반환
    }

    @PostMapping("/inquiry/{inquiryId}")
    @ResponseBody
    fun replyInquiry(
        @PathVariable inquiryId: Long,
        @RequestParam("reply") reply: String,
        session: HttpSession
    ): ResponseEntity<String> { // 관리자 (admin) - 문의 글 답변 관련 처리 : 처음 답변 작성할 때
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")

        userInquiryService.saveReplyInquiry(userInquiryService.getInquiryById(inquiryId), reply) // 해당 문의에다가 답변 기록

        return ResponseEntity.ok("Reply saved successfully") // fetch API => 경로 처리 : ResponseEntity 가 ok => loadInquiryDetail(inquiryId) 실행함
    }

    @PostMapping("/inquiry/edit/{inquiryId}")
    @ResponseBody
    fun editReply(
        @PathVariable inquiryId: Long,
        @RequestParam("replyEdit") reply: String,
        session: HttpSession
    ): ResponseEntity<String> { // 관리자 (admin) - 문의 답변 수정 처리 함수
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")

        if (reply.isEmpty()) { // 수정한 답변이 공란이다?
            return ResponseEntity.badRequest().body("Reply cannot be empty")
        }

        userInquiryService.saveReplyInquiry(userInquiryService.getInquiryById(inquiryId), reply) // 해당 문의에다가 수정된 답변 기록 (갱신)

        return ResponseEntity.ok("Reply edited successfully") // fetch API => 경로 처리 : ResponseEntity 가 ok => loadInquiryDetail(inquiryId) 실행함
    }
}

