package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.controller.form.ReplyInquiryForm
import com.surveyapp.kotlinsurvey.domain.inquiry.InquiryState
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.repository.UserInquiryRepository
import jakarta.servlet.http.HttpSession
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Transactional
class UserInquiryService(
    @Autowired private val userInquiryRepository: UserInquiryRepository
    ) {

    // 문의 게시 글 정보 저장
    fun saveInquiry(inquiry: UserInquiry) {
        userInquiryRepository.saveInquiry(inquiry)
    }

    // 문의 게시 글 조회
    fun getInquiryById(inquiryId: Long): UserInquiry { return userInquiryRepository.getInquiryById(inquiryId) }

    // 모든 문의 게시 글 조회
    fun getInquiryList(): List<UserInquiry>? { return userInquiryRepository.getInquiryList() }


    // 문의 답변 저장
    fun saveReplyInquiry(inquiry: UserInquiry, reply: String)
    {
        inquiry.answerDate = LocalDateTime.now()
        inquiry.reply = reply
        inquiry.status = InquiryState.COMPLETE
    }

    // 문의 답변 수정
    fun editReplyInquiry(inquiryId: Long, reply: String)
    {
        val inquiry = userInquiryRepository.getInquiryById(inquiryId)
        inquiry.reply = reply // 답변 수정
        inquiry.answerDate = LocalDateTime.now()  // 답변 수정 날짜 변경

        userInquiryRepository.saveInquiry(inquiry) // 저장
    }
}