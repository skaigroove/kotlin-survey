/* WebSocketConfig.kt
* SurveyBay - 채팅 관련 클래스 : WebSocketMessageBrokerConfigurer 인터페이스 구현
* WebSocketMessageBrokerConfigurer : 메시지를 보내는 이와 받는 이 사이에서 통신을 중재하는 역할
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 참고 : https://basketdeveloper.tistory.com/entry/Kotlin-Spring-boot-WebSocket-%EC%82%AC%EC%9A%A9%EB%B2%95
* 추가 구현 : 실시간 채팅에 참여한 회원 목록 처리
* 프로그램 최종 수정 : 2024.6.3. 실시간 채팅에 참여한 회원 목록 갱신 처리 (로그아웃 시 목록에서 삭제)
*/

package com.surveyapp.kotlinsurvey.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) { // STOMP 엔드 포인트 등록
        registry.addEndpoint("/ws").withSockJS() // 채팅 참여자가 WebSocket 서버에 연결하기 위해 사용할 엔드 포인트를 '/ws' 로 등록 + SockJS 활성화
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) { // 메시지 브로커 구성
        config.setApplicationDestinationPrefixes("/app") // 애플리케이션에서 처리할 메시지의 경로 프리픽스를 '/app' 으로 설정 : 이 경로를 통해서 메시지를 서버로 전송함
        config.enableSimpleBroker("/topic") // SimpleBroker 를 활성화하여 '/topic' 으로 시작하는 경로를 가진 메시지를 브로킹함 : 이 경로를 통해서 메시지를 수신함
    }
}