/* UserProfileController.kt
* SurveyBay - 회원 : 마이 페이지 관련 Controller
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.7. 응답 기한이 끝난 설문은 답변 수정 불가하도록 추가 처리
*/

package com.surveyapp.kotlinsurvey.controller.user

import com.surveyapp.kotlinsurvey.controller.form.EditAnswerListForm
import com.surveyapp.kotlinsurvey.controller.form.UserForm
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.SurveyParticipationService
import com.surveyapp.kotlinsurvey.service.SurveyService
import org.springframework.ui.Model
import com.surveyapp.kotlinsurvey.service.UserService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@Controller
@RequestMapping("/user")
class UserProfileController(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val surveyService: SurveyService,
    @Autowired private val surveyParticipationService: SurveyParticipationService
) {

    @GetMapping("/profile")
    fun viewUserProfile(session: HttpSession, model: Model): String { // 마이 페이지 보여 줄 때
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        // session으로부터 loginId를 받아와, 해당 유저를 찾는다.
        val sessionLoginId = session.getAttribute("loginId") as String
        val user = userService.findUserByLoginId(sessionLoginId)

        // 패스워드 가공
        val maskedPassword = if (user!!.password.length > 2) {
            user.password.take(2) + "*".repeat(user.password.length - 2)
        } else {
            user.password
        }

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username)

        val userCopy = User(
            userId = user.userId,
            loginId = user.loginId,
            password = maskedPassword, // 가공된 패스워드 사용
            name = user.name,
            birthDate = user.birthDate,
            genderType = user.genderType,
            phoneNumber = user.phoneNumber,
            userType = user.userType
        )

        model.addAttribute("loginUser", userCopy) // model 속성으로 추가

        /* 내가 작성한 surveylist 조회를 위한 부분*/
        val surveyList = surveyService.getUserSurveyList(sessionLoginId)
        model.addAttribute("surveyList", surveyList)

        // 해당 회원이 참여한 설문 목록 반환
        val participatedSurveyList = surveyParticipationService.getUserParticipatedSurveyList(sessionLoginId)
        model.addAttribute("participatedSurveyList", participatedSurveyList)

        return "user-auth/user-profile/profile-main-user" // 경로 반환
    }

    @GetMapping("/profile/edit")
    fun createUserForm(model: Model, session: HttpSession):String{ // 회원 정보 수정
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        // session으로부터 loginId를 받아와, 해당 유저를 찾는다.
        val sessionLoginId = session.getAttribute("loginId") as String
        val user : User? = userService.findUserByLoginId(sessionLoginId)

        // 찾은 user의 값을 userForm에 삽입한다.
        model.addAttribute("userForm", UserForm(user!!.loginId, user.password, user.name, user.birthDate, user.genderType, user.phoneNumber)) // 초기화한 폼을 모델에 추가
        model.addAttribute("loginUser",user)

        // 세션에서 사용자 이름 가져오기 -> 헤더의 사용자 정보 표시
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username)

        return "user-auth/user-profile/edit-profile-user" // 경로 반환
    }

    @PostMapping("/profile/edit")
    fun editUserInformation(
        @Valid @ModelAttribute("user") userForm: UserForm,
        model: Model,
        result: BindingResult,
        session: HttpSession,
    ): String { // 회원 정보 수정 관련 처리
        if (result.hasErrors()) {
            return "user-auth/user-profile/profile-main-user"
        }

        val sessionUser = userService.findUserByLoginId(session.getAttribute("loginId") as String)
        val formUser = userRepository.findByPhoneNumber(userForm.phoneNumber)

        if (formUser != null && userService.validateDuplicateUserByPhoneNum(formUser) && userForm.phoneNumber != sessionUser?.phoneNumber) {
            return "redirect:/user/profile/edit?phoneNumberDuplicatedError=true"
        }

        val loginId = session.getAttribute("loginId") as String
        userService.updateUser(loginId, userForm) // 수정된 회원 정보 갱신

        return "redirect:/user/profile" // 경로 반환
    }

    @PostMapping("/survey/delete/{surveyId}")
    fun deleteCreatedSurvey(@PathVariable surveyId: Long, session: HttpSession): String { // 해당 회원이 생성한 설문 삭제 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String
        val survey = surveyService.getSurveyById(surveyId)

        // Check if the user owns the survey
        if (survey?.user?.loginId == sessionLoginId) {
            surveyService.deleteSurvey(surveyId) // 설문 삭제
        }

        return "redirect:/user/profile" // 경로 반환
    }

    @GetMapping("/survey/view/{surveyId}")
    fun viewCreatedSurvey(@PathVariable surveyId: Long, model: Model, session: HttpSession): String { // 해당 회원이 생성한 설문 상세 보기 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String
        val survey = surveyService.getSurveyById(surveyId)

        model.addAttribute("survey", survey) // survey 를 model 속성으로 추가

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username) // user 이름을 model 속성으로 추가

        if (survey != null && survey.user.loginId == sessionLoginId) { // 해당 설문이 존재하고 해당 설문 생성자와 sessionLoginId 에 해당하는 회원이 동일인이면
            model.addAttribute("survey", survey) // survey 를 model 속성으로 추가

            return "user-auth/user-profile/self-created-survey-view" // 경로 반환
        }

        return "redirect:/user/profile" // 경로 반환 - 설문 상세 보기 불가 : 설문이 존재하지 않거나 해당 설문 생성자와 로그인 회원이 다른 사람
    }

    // 참여 정보 보기
    @GetMapping("/participation/view/{participationId}")
    fun viewSurveyParticipation(@PathVariable participationId: Long, model: Model, session: HttpSession): String { // 해당 회원이 참여한 설문의 답변 상세 보기 처리

        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        // loginId를 세션으로부터 받아온다.
        val sessionLoginId = session.getAttribute("loginId") as String

        // 참여 정보를 가져온다
        val participation = surveyService.getParticipationById(participationId)

        // 참여 정보를 모델에 넣는다.
        model.addAttribute("participation", participation)

        // 설문 응답 기한이 끝나면 답변 수정 불가
        var canEditCheck = participation?.survey?.endDate?.isAfter(LocalDate.now()) ?: false // 답변 수정 가능 여부
        if (participation?.survey?.endDate?.isEqual(LocalDate.now()) == true)
        { // 마감 기한 날짜 == 오늘 날짜 ; 답변 수정 가능
            canEditCheck = true
        }
        model.addAttribute("canEditCheck", canEditCheck) // model 에 속성 추가


        // 세션에서 사용자 이름 가져오기 -> 헤더의 사용자 정보 표시
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username)

        // 참여 정보의 user의 loginId와 세션의 loginId가 같으면 참여 정보를 볼 수 있는 페이지로 이동한다.
        if (participation?.user?.loginId == sessionLoginId) {
            // participationId를 모델에 추가한다
            model.addAttribute("participationId", participationId)

            return "user-auth/user-profile/view-participate-user" // 경로 반환
        }

        // loginId 가 다르면 홈으로 리다이렉트
        return "redirect:/home"
    }

    @GetMapping("/participation/edit/{participationId}")
    fun editSurveyParticipation(
        @PathVariable participationId: Long,
        model: Model,
        session: HttpSession
    ): String { // 설문 답변 수정 처리

        // login 여부를 확인하고 로그인이 안 되어 있으면 홈으로 리다이렉트, 로그인 되어 있으면 model에 사용자 loginId를 넣는다.
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String

        // 참여 정보를 가져와 model에 넣는다.
        val participation = surveyService.getParticipationById(participationId)
        model.addAttribute("participation", participation)

        // 세션에서 사용자 이름 가져오기 -> 헤더의 사용자 정보 표시
        val username = session.getAttribute("username")
        println("username: $username") // 여기서 println 대신 logger 사용을 권장
        model.addAttribute("username", username)

        // 참여 정보의 user의 loginId와 세션의 loginId가 같으면 참여 정보를 수정할 수 있는 페이지로 이동한다.
        if (participation?.user?.loginId == sessionLoginId) {
            model.addAttribute("participationId", participationId) // participationId를 모델에 추가

            // AnswerListForm을 통해 답변을 받기 위해 기존 답변들을 설정 후 모델에 추가
            val answerListForm = surveyService.getAnswerListFormByParticipation(participation)
            model.addAttribute("answerListForm", answerListForm)

            return "user-auth/user-profile/edit-participate-user" // 경로 반환
        }

        return "redirect:/user/profile" // 경로 반환 - 설문 답변 수정 불가
    }

    @PostMapping("/participation/edit/{participationId}")
    fun updateSurveyParticipation(
        @PathVariable participationId: Long,
        @ModelAttribute editAnswerListForm: EditAnswerListForm,
        session: HttpSession
    ): String { // 설문 답변 수정한 것 갱신 처리
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String

        // 참여 정보를 가져온다
        val participation = surveyService.getParticipationById(participationId)

        if (participation?.user?.loginId == sessionLoginId) {
            // AnswerListForm을 통해 답변을 업데이트
            surveyService.updateAnswers(participation, editAnswerListForm)

            return "redirect:/user/participation/view/{participationId}" // 경로 반환
        }

        return "redirect:/user/profile" // 경로 반환
    }

}