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
        | adminSurveyList() | 설문 목록 관련 처리를 하는 함수로, 작성된 모든 설문을 model 속성으로 추가하고 경로를 반환한다. |
        | adminDetailSurvey() | 설문 상세 보기 관련 처리를 하는 함수로, 해당 설문에 대한 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | adminSurveyDelete() | 설문 삭제 관련 처리를 하는 함수로, 해당 설문을 삭제하고 ResponseEntity.ok() 를 반환한다. |
- Global
    
    : 전역에서 사용하는 Controller
    
    1. ChatController : `실시간 채팅` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | sendMessage() | 메시지 전송 관련 처리를 하는 함수로, 메시지를 반환한다. |
        | addUser() | 실시간 채팅 참여 사용자 관련 처리를 하는 함수로, 해당 사용자 이름을 실시간 채팅 참여자 목록에 추가하고 그 사용자가 채팅에 참여했다는 메시지를 broadcasting 한다. |
        | sendHttpMessage() | ~~ 관련 처리를 하는 함수로, ~~ ResponseEntity.ok() 를 반환한다. |
        | getOnlineUsers() | 실시간 채팅에 참여한 사용자 목록 관련 처리를 하는 함수로, 현재 참여한 사용자 목록을 모든 채팅 참여자에게 알린다. |
    2. HomeController : `Home` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | home() | 회원 (Client) Home 관련 처리를 하는 함수로, 해당 회원의 이름을 model 속성으로 추가하고 경로를 반환한다. |
        | logout() | 로그아웃 관련 처리를 하는 함수로, 세션을 무효화하여 로그아웃한 후 경로를 반환한다. |
        | getSessionUsername() | 사용자 이름 관련 처리를 하는 함수로, 세션에서 사용자 이름을 얻어서 ~~ 반환한다. |
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
    
    1. UserInquiryController : 회원 (Client) 계정으로 `1:1 문의 관리` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | listInquiry() | 문의 목록 관련 처리를 하는 함수로, 작성된 모든 문의 글을 model 속성으로 추가하고 경로를 반환한다. |
        | detailInquiry() | 문의 글 상세 보기 관련 처리를 하는 함수로, 해당 문의 글에 대한 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | createInquiryForm() | 문의 글 작성 관련 처리를 하는 함수로, UserInquiryForm() 을 model 속성으로 추가하고 경로를 반환한다. |
        | writeInquiry() | 문의 글 작성 관련 처리를 하는 함수로, 작성한 문의 글을 저장하고 경로를 반환한다. |
        | createInquiry() | 문의 글 작성 관련 처리를 하는 함수로, inquiry 를 생성하여 반환한다. |
    2. UserSurveyController : 관리자 계정으로 `설문 관리` 관련 기능 처리 
        
        
        | Method | Describe |
        | --- | --- |
        | adminSurveyList() | 설문 목록 관련 처리를 하는 함수로, 작성된 모든 설문을 model 속성으로 추가하고 경로를 반환한다. |
        | adminDetailSurvey() | 설문 상세 보기 관련 처리를 하는 함수로, 해당 설문에 대한 정보를 model 속성으로 추가하고 경로를 반환한다. |
        | adminSurveyDelete() | 설문 삭제 관련 처리를 하는 함수로, 해당 설문을 삭제하고 ResponseEntity.ok() 를 반환한다. |
    3. UserProfileController : 관리자 계정으로 `회원 관리` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | adminMemberList() | 회원 목록 관련 처리를 하는 함수로, 가입한 모든 회원을 model 속성으로 추가하고 경로를 반환한다. |
        | adminMemberDetail() | 회원 정보 상세 보기 관련 처리를 하는 함수로, 해당 회원에 대한 정보와 문자열 형식을 변환 처리한 전화번호를 model 속성으로 추가하고 경로를 반환한다. |
        | formatPhoneNumber() | 전화번호를 010.0000.0000 형식으로 변환하여 반환한다. |
        | adminMemberDelete() | 회원 탈퇴 관련 처리를 하는 함수로, 해당 회원 정보를 삭제하고 ResponseEntity.ok() 를 반환한다. |
    4. UserStatisticController : 관리자 계정으로 `회원 관리` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | adminMemberList() | 회원 목록 관련 처리를 하는 함수로, 가입한 모든 회원을 model 속성으로 추가하고 경로를 반환한다. |
        | adminMemberDetail() | 회원 정보 상세 보기 관련 처리를 하는 함수로, 해당 회원에 대한 정보와 문자열 형식을 변환 처리한 전화번호를 model 속성으로 추가하고 경로를 반환한다. |
        | formatPhoneNumber() | 전화번호를 010.0000.0000 형식으로 변환하여 반환한다. |
        | adminMemberDelete() | 회원 탈퇴 관련 처리를 하는 함수로, 해당 회원 정보를 삭제하고 ResponseEntity.ok() 를 반환한다. |
    5. SurveyStatisticController : 관리자 계정으로 `회원 관리` 관련 기능 처리
        
        
        | Method | Describe |
        | --- | --- |
        | adminMemberList() | 회원 목록 관련 처리를 하는 함수로, 가입한 모든 회원을 model 속성으로 추가하고 경로를 반환한다. |
        | adminMemberDetail() | 회원 정보 상세 보기 관련 처리를 하는 함수로, 해당 회원에 대한 정보와 문자열 형식을 변환 처리한 전화번호를 model 속성으로 추가하고 경로를 반환한다. |
        | formatPhoneNumber() | 전화번호를 010.0000.0000 형식으로 변환하여 반환한다. |
        | adminMemberDelete() | 회원 탈퇴 관련 처리를 하는 함수로, 해당 회원 정보를 삭제하고 ResponseEntity.ok() 를 반환한다. |