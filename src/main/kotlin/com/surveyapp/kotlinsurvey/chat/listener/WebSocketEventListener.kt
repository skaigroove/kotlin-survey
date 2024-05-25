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

    private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)
    private val onlineUsers = mutableSetOf<String>()

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        logger.info("Received a new web socket connection")
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = event.message.headers[SimpMessageHeaderAccessor::class.java.name] as SimpMessageHeaderAccessor
        val username = headerAccessor.sessionAttributes?.get("username") as String?

        if (username != null) {
            logger.info("User Disconnected: $username")
            onlineUsers.remove(username)
            val chatMessage = ChatMessage(sender = username, content = "", type = MessageType.LEAVE)
            messagingTemplate.convertAndSend("/topic/public", chatMessage)
            messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers)
        }
    }

    fun addUser(username: String) {
        onlineUsers.add(username)
        messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers)
    }

    fun getOnlineUsers(): Set<String> {
        return onlineUsers
    }
}
