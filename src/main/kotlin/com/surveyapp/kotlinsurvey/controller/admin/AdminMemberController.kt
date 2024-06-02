package com.surveyapp.kotlinsurvey.controller.admin

import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping("/admin")
class AdminMemberController (
    @Autowired private val userService: UserService,
) {
    @GetMapping("/member")
    fun adminMemberList(model: Model, session:HttpSession): String { // 관리자 (admin) - 회원 관리 관련 처리 : 모든 회원 목록 조회
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val memberList = userService.getUserList() // 모든 회원 목록을 list 로 get

        model.addAttribute("adminMemberList", memberList) // 모든 회원 목록을 model 속성으로 추가

        return "adminMember/adminMemberList" // 경로 반환
    }

    @GetMapping("/member/{userId}")
    fun adminMemberDetail(@PathVariable userId: Long, model: Model, session:HttpSession): String { // 관리자 (admin) - 회원 정보 열람 (상세 보기) 관련 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val member = userService.findOne(userId) // userId 에 해당하는 사용자 get

        model.addAttribute("adminMember", member) // 해당 회원을 model 속성으로 추가
        model.addAttribute("adminMemberPhoneNumber", formatPhoneNumber(member.phoneNumber, "-"))

        return "adminMember/adminMemberDetail" // 경로 반환
    }

    fun formatPhoneNumber(phoneNumber: String, separator: String = "."): String { // 전화번호 010.0000.0000 형식으로 변환하는 함수 // . 은 default, separator 에 따라 달라짐
        val regex = "(\\d{3})(\\d{4})(\\d{4})".toRegex()
        return regex.replace(phoneNumber, "$1$separator$2$separator$3")
    }



    @PostMapping("/member/delete/{userId}")
    fun adminMemberDelete(@PathVariable userId: Long, session: HttpSession): String { // 관리자 (admin) - 회원 탈퇴 관련 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        userService.deleteUserByUserId(userId) // 해당 회원 정보 삭제 => 탈퇴

        return "redirect:/admin/member"
        //return ""
    }



/*
    @PostMapping("/member/delete/{userId}")
    fun adminMemberDelete(@PathVariable userId: Long, model: Model, session: HttpSession): ResponseEntity<String> {
        if (userService.checkLogin(session) == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")

        userService.deleteUserByUserId(userId)
        val updatedMemberList = adminMemberList(model, session)

        return ResponseEntity.ok("User deleted successfully")
    }


 */

}