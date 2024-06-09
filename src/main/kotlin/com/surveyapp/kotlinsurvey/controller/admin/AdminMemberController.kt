/* AdminMemberController.kt
* SurveyBay - 관리자 : 회원 문의 관리 관련 Controller
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.6. 회원 탈퇴 처리 (회원 정보 삭제) 시 경로 처리 해결
*/

package com.surveyapp.kotlinsurvey.controller.admin

import com.surveyapp.kotlinsurvey.chat.listener.WebSocketEventListener
import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping("/admin")
class AdminMemberController (
    @Autowired private val userService: UserService,
) {
    private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java) // 로그 확인용

    @GetMapping("/member")
    fun adminMemberList(model: Model, session:HttpSession): String { // 관리자 (admin) - 회원 관리 관련 처리 : 모든 회원 목록 조회
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val memberList = userService.getUserList() // 모든 회원 목록을 list 로 get

        model.addAttribute("adminMemberList", memberList) // 모든 회원 목록을 model 속성으로 추가

        return "admin-auth/admin-member/admin-member-list" // 경로 반환
    }

    @GetMapping("/member/{userId}")
    fun adminMemberDetail(@PathVariable userId: Long, model: Model, session:HttpSession): String { // 관리자 (admin) - 회원 정보 열람 (상세 보기) 관련 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val member = userService.findOne(userId) // userId 에 해당하는 사용자 get

        model.addAttribute("adminMember", member) // 해당 회원을 model 속성으로 추가
        model.addAttribute("adminMemberPhoneNumber", formatPhoneNumber(member.phoneNumber, "-")) // 전화번호 속성 추가 : 한눈에 보기 쉽도로고 전화번호 문자열 형식 변환

        return "admin-auth/admin-member/admin-member-detail" // 경로 반환
    }

    fun formatPhoneNumber(phoneNumber: String, separator: String = "."): String { // 전화번호 010.0000.0000 형식으로 변환하는 함수 // . 은 default, separator 에 따라 달라짐
        val regex = "(\\d{3})(\\d{4})(\\d{4})".toRegex() // 정규 표현식 정의 : 각각 3개, 4개, 4개로 나눔
        return regex.replace(phoneNumber, "$1$separator$2$separator$3") // 문자열 치환 : 3 기호 4 기호 4
    }

    @PostMapping("/member/delete/{userId}")
    @ResponseBody
    fun adminMemberDelete(@PathVariable userId: Long, session: HttpSession): ResponseEntity<String> { // 관리자 (admin) - 회원 탈퇴 관련 처리
        // 로그인 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")

        logger.info("adminMemberDelete() run.")

        // 회원 탈퇴 처리
        userService.deleteUserByUserId(userId) // 해당 회원 정보 삭제 => 탈퇴
        logger.info("userService.deleteUserByUserId($userId) done.")

        return ResponseEntity.ok("Member deleted successfully") // fetch API => 경로 처리 : ResponseEntity 가 ok => loadMemberList() 실행
    }
}
