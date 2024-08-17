# Web Server Module

## 1. Controller

- Admin
    
    : 관리자 (Admin) 계정으로 Web 에 접속했을 때 
    
    1. AdminInquiryController : 관리자 계정으로 `1:1 문의 관리` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | adminInquiryList() | 문의 목록 관련 처리를 하는 함수로, 작성된 모든 문의 글을 model 속성으로 추가하고 경로를 반환한다. |
        | adminDetailInquiry() | 문의 글 상세 보기 관련 처리를 하는 함수로, 해당 문의 글에 대한 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | replyInquiry() | 문의 글에 대한 답변 작성 관련 처리를 하는 함수로, 해당 문의에다가 답변을 기록하고 ResponseEntity.ok() 를 반환한다. |
        | editReply() | 문의 글에 대한 답변 수정 관련 처리를 하는 함수로, 해당 문의에다가 수정된 답변을 기록하고 ResponseEntity.ok() 를 반환한다. |
    2. AdminMemberController : 관리자 계정으로 `회원 관리`  관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | adminMemberList() | 회원 목록 관련 처리를 하는 함수로, 가입한 모든 회원을 model 속성으로 추가하고 경로를 반환한다. |
        | adminMemberDetail() | 회원 정보 상세 보기 관련 처리를 하는 함수로, 해당 회원에 대한 정보와 문자열 형식을 변환 처리한 전화번호를 model 속성으로 추가하고 경로를 반환한다. |
        | formatPhoneNumber() | 전화번호를 010.0000.0000 형식으로 변환하여 반환한다. |
        | adminMemberDelete() | 회원 탈퇴 관련 처리를 하는 함수로, 해당 회원 정보를 삭제하고 ResponseEntity.ok() 를 반환한다. |
    3. AdminSurveyController : 관리자 계정으로 `설문 관리` 관련 기능 처리 
        
        
        | Method | Describe |
        | --- | --- |
        | adminSurveyList() | 설문 목록 관련 처리를 하는 함수로, 생성된 모든 설문을 model 속성으로 추가하고 경로를 반환한다. |
        | adminDetailSurvey() | 설문 상세 보기 관련 처리를 하는 함수로, 해당 설문에 대한 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | adminSurveyDelete() | 설문 삭제 관련 처리를 하는 함수로, 해당 설문을 삭제하고 ResponseEntity.ok() 를 반환한다. |
- Global
    
    : 전역에서 사용하는 Controller
    
    1. ChatController : `실시간 채팅` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | sendMessage() | 메시지 전송 관련 처리를 하는 함수로, 메시지를 반환한다. |
        | addUser() | 실시간 채팅 참여 사용자 관련 처리를 하는 함수로, 해당 사용자 이름을 실시간 채팅 참여자 목록에 추가하고 그 사용자가 채팅에 참여했다는 메시지를 broadcasting 한다. |
        | sendHttpMessage() | 요청 본문에 담긴 메시지인ChatMessage 객체를 WebSocket 경로(/topic/public) 로 전송하고, 응답이 잘 전송 되었다면 HTTP 200 을 반환한다. |
        | getOnlineUsers() | 실시간 채팅에 참여한 사용자 목록 관련 처리를 하는 함수로, 현재 참여한 사용자 목록을 모든 채팅 참여자에게 알린다. |
    2. HomeController : `Home` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | home() | 회원 (Client) Home 관련 처리를 하는 함수로, 해당 회원의 이름을 model 속성으로 추가하고 경로를 반환한다. |
        | logout() | 로그아웃 관련 처리를 하는 함수로, 세션을 무효화하여 로그아웃한 후 경로를 반환한다. |
        | getSessionUsername() | 사용자 이름 관련 처리를 하는 함수로, 세션에서 사용자 이름을 얻어 map에 추가한 JSON 형태로 반환한다. |
        | adminHome() | 관리자 (Admin) Home 관련 처리를 하는 함수로, 해당 관리자의 이름을 model 속성으로 추가하고 경로를 반환한다. |
    3. UserController : `회원 가입`, `로그인` 관련 기능 처리 
        
        
        | Method | Describe |
        | --- | --- |
        | createForm() | 회원 가입 관련 처리를 하는 함수로, UserForm() 을 model 속성으로 추가하고 경로를 반환한다. |
        | createUser() | 회원 가입 관련 처리를 하는 함수로, 회원 가입 처리 후 경로를 반환한다. |
        | loginForm() | 로그인 관련 처리를 하는 함수로, LoginForm() 을 model 속성으로 추가하고 경로를 반환한다. |
        | login() | 로그인 관련 처리를 하는 함수로, 로그인 성공과 실패에 대해 각각 처리한다. |
- User
    
    : 회원 (Client) 계정으로 Web 에 접속했을 때 
    
    1. UserInquiryController : 회원 (Client) 계정으로 `1:1 문의` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | listInquiry() | 문의 목록 관련 처리를 하는 함수로, 작성된 모든 문의 글을 model 속성으로 추가하고 경로를 반환한다. |
        | detailInquiry() | 문의 글 상세 보기 관련 처리를 하는 함수로, 해당 문의 글에 대한 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | createInquiryForm() | 문의 글 작성 관련 처리를 하는 함수로, UserInquiryForm() 을 model 속성으로 추가하고 경로를 반환한다. |
        | writeInquiry() | 문의 글 작성 관련 처리를 하는 함수로, 작성한 문의 글을 저장하고 경로를 반환한다. |
        | createInquiry() | 문의 글 작성 관련 처리를 하는 함수로, inquiry 를 생성하여 반환한다. |
    2. UserSurveyController : 회원 (Client) 계정으로 `설문 조사` 관련 기능 처리 
        
        
        | Method | Describe |
        | --- | --- |
        | viewSurveyList() | 설문 목록 관련 처리를 하는 함수로, 생성된 모든 설문과 해당 회원이 참여한 설문 목록을 model 속성으로 추가하고 경로를 반환한다. |
        | createSurveyForm() | 설문 생성 관련 처리를 하는 함수로, SurveyForm()  과 QuestionForm() 을 createSurvey model 속성으로 추가하고 경로를 반환한다. |
        | createSurvey() | 설문 생성 관련 처리를 하는 함수로, 설문 조사를 생성하여 저장하고 경로를 반환한다. |
        | createAnswerForm() | 설문 참여 (응답) 관련 처리를 하는 함수로, 설문 정보, 질문 목록 등을 model 속성으로 추가하고 경로를 반환한다. |
        | responseSurvey() | 설문 참여 (응답) 관련 처리를 하는 함수로, 해당 설문에 대한 응답을 기록하고 경로를 반환한다. |
    3. UserProfileController :  회원 (Client) 계정으로 `마이 페이지` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | viewUserProfile() | 마이 페이지 첫 화면을 보여 줄 때 처리하는 함수로, 해당 회원의 정보와 생성·참여한 설문 목록을 model 속성으로 추가하고 경로를 반환한다. |
        | createUserForm() | 회원 정보 수정 관련 처리를 하는 함수로, 해당 회원에 대한 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | editUserInformation() | 회원 정보 수정 관련 처리를 하는 함수로, 수정된 회원 정보를 갱신하고 경로를 반환한다. |
        | deleteCreatedSurvey() | 설문 삭제 관련 처리를 하는 함수로, 해당 회원이 생성한 설문 중 삭제하고 싶은 설문을 삭제하고 경로를 반환한다. |
        | viewCreatedSurvey() | 해당 회원이 생성한 설문 상세 보기 관련 처리를 하는 함수로, 해당 설문 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | viewSurveyParticipation() | 해당 회원이 참여한 설문의 답변 상세 보기 관련 처리를 하는 함수로, 해당 설문에 참여한 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | editSurveyParticipation() | 참여한 설문의 답변 수정 관련 처리를 하는 함수로, 해당 설문에 대한 참여 정보와 기존 응답을 model 속성으로 추가하고 경로를 반환한다. |
        | updateSurveyParticipation() | 참여한 설문의 답변 수정 관련 처리를 하는 함수로, 수정된 설문 답변을  갱신하고 경로를 반환한다. |
    4. UserStatisticController : 회원 (Client) 계정으로 `설문 결과` 관련 기능 처리. 
        
        **웹 페이지와 상호 작용** 하는 컨트롤러로, 사용자 통계 데이터를 렌더링하는 **Thymeleaf 템플릿**을 반환
        
        | Method | Describe |
        | --- | --- |
        | showSurveyStatisticPage() | 뷰(View)를 반환하는 역할으로, api에서 통계 관련 데이터를 반환받아 모델에 담고 Thymeleaf Template Engine을 통해 처리한다. |
    5. UserStatisticRestController : 회원 (Client) 계정으로 `설문 답변 통계` 관련 기능 처리. 
        
        **REST API 엔드포인트**를 제공하는 컨트롤러로, 사용자 통계 데이터를 **JSON 형식**으로 반환하여 비동기적으로 사용할 수 있도록 제공.
        
        | Method or Dataclasses | Describe |
        | --- | --- |
        | getSurveyStatistics() | 특정 설문 조사에 대한 통계를 계산하고 JSON 형식의 응답을 반환하는 함수.
        주관식/객관식에 대한 리스트를 만들어 객관식 답변은 선택된 횟수를 Counting, 주관식 답변은 모든 응답을 저장하여 SurveyStatisticsResponse 로  묶어 반환한다. |
        | Data class SurveyStatisticsResponse | 생성자명, 설문 정보, 객관식, 주관식, 주관식 질문의 묶음 데이터 클래스 |
        | Data class SurveyResponse | 설문 정보에 대한 데이터 클래스. 제목, 생성일자, 만료 일자, 설문 설명이 포함되었다. |
        | Data class Objective StatisticResponse | 객관식 질문에 대한 데이터 클래스. 객관식 질문에 대한 아이디, 선택지, 선택된 옵션, 선택된 옵션에 대한 카운팅 정보가 포함되었다. |
        | Data class SubjectiveStatisticResponse | 주관식 질문에 대한 데이터 클래스. 주관식 질문에 대한 아이디, 주관식 설문 description 이 포함되었다. |