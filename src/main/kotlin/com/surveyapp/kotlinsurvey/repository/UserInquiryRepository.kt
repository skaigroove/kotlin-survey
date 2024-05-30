package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserInquiryRepository : JpaRepository<UserInquiry, Long> {


}
