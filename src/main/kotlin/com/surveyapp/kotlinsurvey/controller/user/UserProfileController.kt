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

@Controller
@RequestMapping("/user")
class UserProfileController(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val surveyService: SurveyService,
    @Autowired private val surveyParticipationService: SurveyParticipationService
) {

    @GetMapping("/profile")
    fun viewUserProfile(session: HttpSession, model: Model): String {
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
        model.addAttribute("username", username);

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

        model.addAttribute("loginUser", userCopy)

        /* 내가 작성한 surveylist 조회를 위한 부분*/
        val surveyList = surveyService.getUserSurveyList(sessionLoginId)
        model.addAttribute("surveyList", surveyList)

        val participatedSurveyList = surveyParticipationService.getUserParticipatedSurveyList(sessionLoginId)
        model.addAttribute("participatedSurveyList", participatedSurveyList)

        return "user-auth/user-profile/profile-main-user"
    }

    @GetMapping("/profile/edit")
    fun createUserForm(model: Model, session: HttpSession):String{
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        // session으로부터 loginId를 받아와, 해당 유저를 찾는다.
        val sessionLoginId = session.getAttribute("loginId") as String
        val user : User? = userService.findUserByLoginId(sessionLoginId)

        // 찾은 유저의 값들을 userForm에 삽입한다.
        model.addAttribute("userForm", UserForm(user!!.loginId, user.password, user.name, user.birthDate, user.genderType, user.phoneNumber)) // 초기화한 폼을 모델에 추가
        model.addAttribute("loginUser",user)

        // 세션에서 사용자 이름 가져오기 -> 헤더의 사용자 정보 표시
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username)

        return "user-auth/user-profile/edit-profile-user"
    }

    @PostMapping("/profile/edit")
    fun editUserInformation(
        @Valid @ModelAttribute("user") userForm: UserForm,
        model: Model,
        result: BindingResult,
        session: HttpSession,
    ): String {
        if (result.hasErrors()) {
            return "user-auth/user-profile/profile-main-user"
        }

        val sessionUser = userService.findUserByLoginId(session.getAttribute("loginId") as String)
        val formUser = userRepository.findByPhoneNumber(userForm.phoneNumber)

        if (formUser != null && userService.validateDuplicateUserByPhoneNum(formUser) && userForm.phoneNumber != sessionUser?.phoneNumber) {
            return "redirect:/user/profile/edit?phoneNumberDuplicatedError=true"
        }

        val loginId = session.getAttribute("loginId") as String
        val user = userService.updateUser(loginId, userForm)

        return "redirect:/user/profile"
    }

    @PostMapping("/survey/delete/{surveyId}")
    fun deleteCreatedSurvey(@PathVariable surveyId: Long, session: HttpSession): String {
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String
        val survey = surveyService.getSurveyById(surveyId)

        // Check if the user owns the survey
        if (survey?.user?.loginId == sessionLoginId) {
            surveyService.deleteSurvey(surveyId)
        }

        return "redirect:/user/profile"
    }

    @GetMapping("/survey/view/{surveyId}")
    fun viewCreatedSurvey(@PathVariable surveyId: Long, model: Model, session: HttpSession): String {
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String
        val survey = surveyService.getSurveyById(surveyId)

        model.addAttribute("survey", survey)

        // 세션에서 사용자 이름 가져오기
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username);

        if (survey != null && survey.user.loginId == sessionLoginId) {
            model.addAttribute("survey", survey)
            return "user-auth/user-profile/self-created-survey-view" // 여기에 self-created-survey-view.html 파일을 작성해야 합니다.
        }

        return "redirect:/user/profile"
    }

    @GetMapping("/participation/view/{participationId}")
    fun viewSurveyParticipation(@PathVariable participationId: Long, model: Model, session: HttpSession): String {
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String

        // 참여 정보를 가져온다
        val participation = surveyService.getParticipationById(participationId)

        model.addAttribute("participation", participation)

        // 세션에서 사용자 이름 가져오기 -> 헤더의 사용자 정보 표시
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username)

        if (participation?.user?.loginId == sessionLoginId) {
            model.addAttribute("participationId", participationId) // participationId를 모델에 추가
            return "user-auth/user-profile/view-participate-user"
        }

        return "redirect:/user/profile"
    }

    @GetMapping("/participation/edit/{participationId}")
    fun editSurveyParticipation(
        @PathVariable participationId: Long,
        model: Model,
        session: HttpSession
    ): String {

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

            return "user-auth/user-profile/edit-participate-user"
        }

        return "redirect:/user/profile"
    }

    @PostMapping("/participation/edit/{participationId}")
    fun updateSurveyParticipation(
        @PathVariable participationId: Long,
        @ModelAttribute editAnswerListForm: EditAnswerListForm,
        session: HttpSession
    ): String {
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String

        // 참여 정보를 가져온다
        val participation = surveyService.getParticipationById(participationId)

        if (participation?.user?.loginId == sessionLoginId) {
            // AnswerListForm을 통해 답변을 업데이트
            surveyService.updateAnswers(participation, editAnswerListForm)
            return "redirect:/user/participation/view/{participationId}"
        }

        return "redirect:/user/profile"
    }

}