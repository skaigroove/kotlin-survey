/* UserInquiryRepository.kt
* SurveyBay - 1:1 문의 관련 Repository 클래스
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.5.21.
*/

package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.inquiry.InquiryState
import com.surveyapp.kotlinsurvey.domain.inquiry.UserInquiry
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
@Transactional
class UserInquiryRepository(
    @PersistenceContext // 생성자 주입 자동화 어노테이션
    private val em: EntityManager, // 생성자 주입
) {

    fun saveInquiry(inquiry:UserInquiry) { // 1:1 문의 기록
        em.persist(inquiry)
    }

    /* inquiryId 를 기준으로 문의 게시 글 조회 (inquiryId 가 unique 하므로, 단일 조회) */
    fun getInquiryById(inquiryId: Long?): UserInquiry {
        return em.find(UserInquiry::class.java, inquiryId) // EntityManager를 사용하기 때문에, java class로 적어준다
    }

    /* 모든 문의 게시 글 조회 */
    fun getInquiryList(): List<UserInquiry>? {
        return em.createQuery("select m from UserInquiry m", UserInquiry::class.java)
            .getResultList()
    }


}