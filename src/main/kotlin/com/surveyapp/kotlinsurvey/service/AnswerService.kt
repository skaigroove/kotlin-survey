package com.surveyapp.kotlinsurvey.service

import com.surveyapp.kotlinsurvey.domain.answer.Answer
import com.surveyapp.kotlinsurvey.repository.AnswerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AnswerService(private val answerRepository: AnswerRepository) {

    @Transactional
    fun saveAnswer(answer: Answer) {
        answerRepository.save(answer)
    }

    fun getAnswersByParticipationId(participationId: Long): List<Answer>? {
        return answerRepository.findBySurveyParticipationParticipationId(participationId)
    }

    fun getAnswersByAnswerIdList(answerIdList: List<Long?>): List<Answer>? {
        return answerRepository.findByAnswerIdList(answerIdList)
    }
}
