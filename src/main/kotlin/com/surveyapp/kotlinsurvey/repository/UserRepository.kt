package com.surveyapp.kotlinsurvey.repository

import com.surveyapp.kotlinsurvey.domain.user.User
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class UserRepository(
    @Autowired @PersistenceContext // 생성자 주입 자동화 어노테이션
    private val em: EntityManager, // 생성자 주입
) {

    // user를 EntityManager에 추가
    fun save(user: User) {
        em.persist(user)
    }

    // user_id(Long)을 받아 유저 객체를 반환
    fun findOne(userId: Long): User {
        return em.find(User::class.java, userId) // user_id가 primary_key 이므로 find() 해준다.
    }

    // 모든 유저들을 찾아 리스트 형태로 반환
    fun findAll(): List<User> {
        return em.createQuery("select m from User m", User::class.java).getResultList()
    }

    // login_id(String : emain 형식)를 기준으로 유저 객체를 반환
    fun findByLoginId(loginId: String): User {
        return em.createQuery("select m from User m where m.loginId = loginId", User::class.java).singleResult
        // User는 단일 객체이므로, query문 작성해서 singleResult로 return 한다.
    }


    // phone_number(String)를 기준으로 유저 객체를 반환
    fun findByPhoneNumber(phoneNumber: String): User {
        return em.createQuery("select m from User m where m.phoneNumber = phoneNumber", User::class.java).singleResult
        // User는 단일 객체이므로, query문 작성해서 singleResult로 return 한다.
    }

}
