package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.controller.form.ReplyInquiryForm
import com.surveyapp.kotlinsurvey.domain.inquiry.InquiryState
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import com.surveyapp.kotlinsurvey.repository.UserInquiryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserInquiryService(private val userInquiryRepository: UserInquiryRepository) {

    @Transactional
    fun saveInquiry(inquiry: UserInquiry) {
        userInquiryRepository.save(inquiry)
    }

    fun getInquiryById(inquiryId: Long): UserInquiry? {
        return userInquiryRepository.findById(inquiryId).orElse(null)
    }

    fun getInquiryList(): List<UserInquiry>? {
        return userInquiryRepository.findAll()
    }

    // 문의 답변 저장
    fun saveReplyInquiry(inquiry: UserInquiry, replyInquiryForm: ReplyInquiryForm)
    {
        inquiry.answerDate = replyInquiryForm.answerDate
        inquiry.reply = replyInquiryForm.reply
        inquiry.status = InquiryState.COMPLETE
    }

    // 문의 답변 수정
    fun editReplyInquiry(inquiryId: Long, reply: String)
    {
        val inquiry = userInquiryRepository.getReferenceById(inquiryId) // 문의글 가져오기
        inquiry.reply = reply // 답변 수정
        inquiry.answerDate = LocalDateTime.now()  // 답변 수정 날짜 변경

        userInquiryRepository.save(inquiry) // 저장
    }
}
