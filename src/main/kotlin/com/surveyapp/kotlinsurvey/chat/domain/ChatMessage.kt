/* ChatMessage.kt
* SurveyBay - 채팅 관련 domain
* 작성자 : 박예림 (21913687), 이홍비 (21912191)
* 참고 : https://basketdeveloper.tistory.com/entry/Kotlin-Spring-boot-WebSocket-%EC%82%AC%EC%9A%A9%EB%B2%95
* 추가 구현 : 실시간 채팅에 참여한 회원 목록 처리
* 프로그램 최종 수정 : 2024.6.3. 실시간 채팅에 참여한 회원 목록 갱신 처리 (로그아웃 시 목록에서 삭제)
*/

package com.surveyapp.kotlinsurvey.chat.domain

data class ChatMessage(
    var type: MessageType, // 메시지 유형
    var content: String?, // 메시지 내용
    var sender: String // 보내는 이
)
