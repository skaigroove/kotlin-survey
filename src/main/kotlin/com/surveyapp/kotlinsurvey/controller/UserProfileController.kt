package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.controller.form.UserForm
import com.surveyapp.kotlinsurvey.domain.user.User
import com.surveyapp.kotlinsurvey.repository.UserRepository
import com.surveyapp.kotlinsurvey.service.AnswerService
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
    @Autowired private val answerService: AnswerService,
) {

    @GetMapping("/profile")
    fun userProfile(session: HttpSession, model: Model): String {
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
        val surveyList = surveyService.getUserSurveys(sessionLoginId)
        model.addAttribute("surveyList", surveyList)

        /* 내가 참여한 surveyList 조회를 위한 부분 */
        val participatedSurveyList = surveyService.findParticipatedSurveysByLoginId(sessionLoginId)
        model.addAttribute("participatedSurveyList", participatedSurveyList)

        return "userProfile"
    }

    @GetMapping("/profile/edit")
    fun editForm(model: Model, session: HttpSession): String {
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        // session으로부터 loginId를 받아와, 해당 유저를 찾는다.
        val sessionLoginId = session.getAttribute("loginId") as String
        val user: User? = userService.findUserByLoginId(sessionLoginId)

        // 찾은 유저의 값들을 userForm에 삽입한다.
        model.addAttribute(
            "userForm",
            UserForm(user!!.loginId, user.password, user.name, user.birthDate, user.genderType, user.phoneNumber)
        ) // 초기화한 폼을 모델에 추가
        model.addAttribute("loginUser", user)

        return "editProfile"
    }

    @PostMapping("/profile/edit")
    fun editUserProfile(
        @Valid @ModelAttribute("user") userForm: UserForm,
        result: BindingResult,
        session: HttpSession,
    ): String {
        if (result.hasErrors()) {
            return "userProfile"
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
    fun deleteSurvey(@PathVariable surveyId: Long, session: HttpSession): String {
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String
        val survey = surveyService.getSurveyById(surveyId)

        // Check if the user owns the survey
        if (survey?.user?.loginId == sessionLoginId) {
            surveyService.deleteSurvey(survey)
        }

        return "redirect:/user/profile"
    }

    @GetMapping("/survey/view/{surveyId}")
    fun viewSurvey(@PathVariable surveyId: Long, model: Model, session: HttpSession): String {
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

        // 생성한 설문지가 있고, 세션에 로그인이 되어 있으면, 설문지를 보여준다.
        if (survey != null && survey.user.loginId == sessionLoginId) {
            model.addAttribute("survey", survey)
            return "surveyView"
        }
        return "redirect:/user/profile"
    }

    @GetMapping("/participation/view/{participationId}")
    fun viewParticipation(@PathVariable participationId: Long, model: Model, session: HttpSession): String {
        // login 여부 확인
        if (userService.checkLogin(session) == null) // 로그인 안 되었음 => null 반환됨
            return "redirect:/"

        val sessionLoginId = session.getAttribute("loginId") as String

        // 참여 정보를 가져온다
        val participation = surveyService.getParticipationById(participationId)
        model.addAttribute("participation", participation)

        // 참여 정보로부터 question 정보를 가져온다
        val questionList = surveyService.getQuestionsBySurveyId(participation?.survey?.surveyId)
        model.addAttribute("questionList", questionList)

        val answerListByParticipation = participation?.answers?.toList()
        val answerIdList = answerListByParticipation!!.map { it.answerId }

        val answerList = answerService.getAnswersByAnswerIdList(answerIdList)
        model.addAttribute("answerList", answerList)

        // 여기서 null로 나옴
        answerList?.forEach {
            print("AnswerType :" + it.answerType)
        }

        // 세션에서 사용자 이름 가져오기 -> 헤더의 사용자 정보 표시
        val username = session.getAttribute("username")
        print("username: $username")
        model.addAttribute("username", username)

        if (participation?.user?.loginId == sessionLoginId) {
            return "participationView"
        }

        return "redirect:/user/profile"
    }


}