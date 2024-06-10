/* ChatController.kt
* SurveyBay - 전역 : 채팅 관련 Controller
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 참고 : https://basketdeveloper.tistory.com/entry/Kotlin-Spring-boot-WebSocket-%EC%82%AC%EC%9A%A9%EB%B2%95
* 추가 구현 : 실시간 채팅에 참여한 회원 목록 처리
* 프로그램 최종 수정 : 2024.6.3. 실시간 채팅에 참여한 회원 목록 갱신 처리 (로그아웃 시 목록에서 삭제)
*/

package com.surveyapp.kotlinsurvey.controller.global

import com.surveyapp.kotlinsurvey.chat.domain.ChatMessage
import com.surveyapp.kotlinsurvey.chat.domain.MessageType
import com.surveyapp.kotlinsurvey.chat.listener.WebSocketEventListener
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ChatController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val webSocketEventListener: WebSocketEventListener
) {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(message: ChatMessage): ChatMessage { // 메시지 전송
        return message // 메시지 반환
    }

    @MessageMapping("/chat.addUser")
    fun addUser(message: ChatMessage, headerAccessor: SimpMessageHeaderAccessor) { // 현재 참여한 사용자 관련 메시지 처리
        val username = message.sender
        headerAccessor.sessionAttributes?.put("username", username)  // 세션에 사용자 이름 저장
        webSocketEventListener.addUser(username) // message 의 sender 를 현재 채팅에 참여한 사용자로 추가

        // message.sender 가 참여했다는 메시지 생성 => “/topic/pulic” 경로로 해당 메시지 전송
        val chatMessage = ChatMessage(sender = username, content = "", type = MessageType.JOIN)
        messagingTemplate.convertAndSend("/topic/public", chatMessage)
    }

    @PostMapping("/chat/sendMessage")
    @ResponseBody
    fun sendHttpMessage(@RequestBody message: ChatMessage): ResponseEntity<Map<String, String>> {
        messagingTemplate.convertAndSend("/topic/public", message)
        val response = mapOf("message" to "Message sent successfully")
        return ResponseEntity.ok(response)
    }

    @MessageMapping("/chat.getOnlineUsers")
    fun getOnlineUsers() { // 실시간 채팅에 참여한 회원 목록 관련 처리
        // 현재 참여한 사용자 목록을 “/topic/onlineUsers” 경로로 전송하여 모든 채팅 참여자에게 알림
        messagingTemplate.convertAndSend("/topic/onlineUsers", webSocketEventListener.getOnlineUsers())
    }
}
