package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.inquiry.InquiryState
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import com.surveyapp.kotlinsurvey.repository.UserInquiryRepository
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
        inquiry.answerDate = LocalDateTime.now() // 답변 기록 시간 저장
        inquiry.reply = reply // 답변 기록
        inquiry.status = InquiryState.COMPLETE // 문의 글 상태 : 답변 완료
    }
}