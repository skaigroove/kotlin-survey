/* WebSocketEventListener.kt
* SurveyBay - 채팅 관련 웹 소켓 이벤트 리스너
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 참고 : https://basketdeveloper.tistory.com/entry/Kotlin-Spring-boot-WebSocket-%EC%82%AC%EC%9A%A9%EB%B2%95
* 추가 구현 : 실시간 채팅에 참여한 회원 목록 처리
* 프로그램 최종 수정 : 2024.6.3. 실시간 채팅에 참여한 회원 목록 갱신 처리 (로그아웃 시 목록에서 삭제)
*/

package com.surveyapp.kotlinsurvey.chat.listener

import com.surveyapp.kotlinsurvey.chat.domain.ChatMessage
import com.surveyapp.kotlinsurvey.chat.domain.MessageType
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener(
    private val messagingTemplate: SimpMessagingTemplate
) {

    private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java) // 로그 객체
    private val onlineUsers = mutableSetOf<String>() // 실시간 채팅에 참여한 사람 목록

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) { // WebSocket 연결 시 처리할 로직
        logger.info("Received a new web socket connection") // 세션 연결 이벤트 발생 => 새로운 웹 소켓 연결 알림
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) { // WebSocket 연결 해제 시 처리할 로직
        val headerAccessor = SimpMessageHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as String?

        if (username != null) {
            logger.info("User Disconnected: $username")
            onlineUsers.remove(username)

            val chatMessage = ChatMessage(sender = username, content = "", type = MessageType.LEAVE)
            messagingTemplate.convertAndSend("/topic/public", chatMessage)
            messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers)
        } else {
            logger.warn("Session disconnect event received with no username")
        }
    }

    fun addUser(username: String) {  // 추가 구현 - 실시간 채팅 참여자 추가
        onlineUsers.add(username) // onlineUsers 에 해당 사용자 추가
        messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers)
    }

    fun getOnlineUsers(): Set<String> { // 추가 구현 - onlineUsers 반환
        return onlineUsers
    }
}
