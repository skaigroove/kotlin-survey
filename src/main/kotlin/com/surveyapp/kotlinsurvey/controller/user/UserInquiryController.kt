/* UserInquiryController.kt
* SurveyBay - 회원 : 1:1 문의 관련 Controller
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.7. 설문 답변 수정 관련 처리
*/

package com.surveyapp.kotlinsurvey.controller.user

import com.surveyapp.kotlinsurvey.controller.form.UserInquiryForm
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
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
import java.time.LocalDateTime

@Controller
@RequestMapping("/home/inquiry")
class UserInquiryController(
    @Autowired private val userInquiryService: UserInquiryService,
    @Autowired private val userService: UserService,
) {
    @GetMapping("")
    fun listInquiry(model: Model, session: HttpSession): String { // 문의 게시 글 목록
        // login 여부 확인
        if (userService.checkLogin(session) == null) // login 안 된 것
            return "redirect:/"

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username) // user 이름을 model 속성으로 추가

        val inquiryList = userInquiryService.getInquiryList() // 문의 글 목록 get

        model.addAttribute("inquiryList", inquiryList) // 문의 글 목록을 model 속성으로 추가

        return "user-auth/user-inquiry/inquiry-list-user" // 경로 반환 : inquiry-list-user.html
    }

    @GetMapping("/post")
    fun createInquiryForm(model: Model, session:HttpSession): String { // 문의 글 생성 폼
        // login 여부 확인
        if (userService.checkLogin(session) == null) // login 안 된 것
            return "redirect:/"

        // model 에 UserInquiryForm 추가
        model.addAttribute("userInquiryForm", UserInquiryForm())

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username) // user 이름을 model 속성으로 추가

        return "user-auth/user-inquiry/inquiry-create" // 경로 반환 : create-inquiry-list-user.html
    }

    @PostMapping("/post")
    fun writeInquiry(
        @Valid @ModelAttribute("userInquiryForm") userInquiryForm: UserInquiryForm,
        session: HttpSession,
        result: BindingResult,
        redirectAttributes: RedirectAttributes,
    ): String { // 1:1 문의 작성 처리

        // userInquiryFrom : binding 오류 확인
        if (result.hasErrors())
        {
            result.allErrors.forEach { error -> println("Error: ${error.defaultMessage}")}
        }

        // login 여부 확인
        val user = userService.checkLogin(session)
        if (user == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        // inquiry 생성
        val inquiry = createInquiry(userInquiryForm, user)

        userInquiryService.saveInquiry(inquiry) // 저장

        return "redirect:/home/inquiry" // 경로 반환
    }

    fun createInquiry(inquiryForm: UserInquiryForm, user: User): UserInquiry { // inquiry 생성

        // UserInquiryForm => UserInquiry 생성자에 넣어 생성
        val inquiry = UserInquiry(null, user, inquiryForm.title, inquiryForm.writeDate, inquiryForm.content, LocalDateTime.now(), "", inquiryForm.state)

        return inquiry
    }

    @GetMapping("/{inquiryId}")
    fun detailInquiry(@PathVariable inquiryId: Long, model: Model, session: HttpSession): String{ // 문의 글 상세 보기 관련 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // login 안 된 것
            return "redirect:/"

        val inquiryPost: UserInquiry = userInquiryService.getInquiryById(inquiryId) // inquiryId 에 해당하는 문의 글 get

        println("Status: ${inquiryPost.status}")

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username) // user 이름을 model 속성으로 추가

        model.addAttribute("inquiryPost", inquiryPost) // 문의 글을 model 속성으로 추가

        return "user-auth/user-inquiry/inquiry-view-user" // 경로 반환
    }

}