package com.surveyapp.kotlinsurvey.controller

import com.surveyapp.kotlinsurvey.chat.domain.ChatMessage
import com.surveyapp.kotlinsurvey.chat.domain.MessageType
import com.surveyapp.kotlinsurvey.chat.listener.WebSocketEventListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class ChatController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val webSocketEventListener: WebSocketEventListener
) {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(message: ChatMessage): ChatMessage {
        return message
    }

    @MessageMapping("/chat.addUser")
    fun addUser(message: ChatMessage) {
        webSocketEventListener.addUser(message.sender)
        val chatMessage = ChatMessage(sender = message.sender, content = "", type = MessageType.JOIN)
        messagingTemplate.convertAndSend("/topic/public", chatMessage)
    }

    @MessageMapping("/chat.getOnlineUsers")
    fun getOnlineUsers() {
        messagingTemplate.convertAndSend("/topic/onlineUsers", webSocketEventListener.getOnlineUsers())
    }
}
