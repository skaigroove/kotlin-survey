/* SurveyParticipationService.kt
* SurveyBay - 설문 참여 관련 서비스 클래스
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 프로그램 최종 수정 : 2024.6.2. repository 수정
*/

package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.survey.Survey
import com.surveyapp.kotlinsurvey.domain.survey.SurveyParticipation
import com.surveyapp.kotlinsurvey.repository.SurveyParticipationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SurveyParticipationService(
    @Autowired private val surveyParticipationRepository: SurveyParticipationRepository,
    ) {
    fun addParticipation(surveyParticipation: SurveyParticipation) { // 설문 참여 기록 저장
       surveyParticipationRepository.saveParticipation(surveyParticipation)
    }

    fun getSurveyParticipationListByUserId(userId: Long): List<SurveyParticipation>? { // userId 를 가진 사용자가 참여한 설문 조사 목록을 반환함
        return surveyParticipationRepository.getSurveyParticipationListByUserId(userId)
    }
    
    fun getSurveyParticipationListBySurveyId(surveyId: Long): List<SurveyParticipation> { // SurveyId 를 이용해서 해당 설문의 참여 목록을 반환함
        return surveyParticipationRepository.getSurveyParticipationListBySurveyId(surveyId)
    }

    fun getUserParticipatedSurveyList(sessionLoginId: String): List<SurveyParticipation>? { // sessionLoginId 를 이용해서 해당 회원이 참여한 설문 참여 목록 반환
        return surveyParticipationRepository.getSurveyParticipationListByLoginId(sessionLoginId)
    }


}