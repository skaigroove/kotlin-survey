# Postman을 이용한 Test 및 Related Diagrams

# 1. Postman을 이용한 BackEnd Integrated Test Video

[https://youtu.be/86-cGhHlRU0?si=FqCNLBRJrBffSRXR](https://youtu.be/86-cGhHlRU0?si=FqCNLBRJrBffSRXR)

# 2. Controller 기반 API Request Logics / Output Images

> Zip file Download
> 

[controllerAPIRequest.zip](Postman%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%86%AB%20Test%20%E1%84%86%E1%85%B5%E1%86%BE%20Related%20Diagrams%202f909d911dea4bddb81c625b08a89472/controllerAPIRequest.zip)

> e.g.
> 

## Run results image

![AdminFunctionTest-Summary.jpeg](Postman%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%86%AB%20Test%20%E1%84%86%E1%85%B5%E1%86%BE%20Related%20Diagrams%202f909d911dea4bddb81c625b08a89472/AdminFunctionTest-Summary.jpeg)

## Run results json file

```kotlin
{
	"id": "0c788a69-d5ce-4ffb-9602-f648f783d668",
	"name": "AdminFunctionTest",
	"timestamp": "2024-06-08T16:16:04.361Z",
	"collection_id": "34580677-7434c7c2-e96f-4d7b-b947-a9d14179039c",
	"folder_id": 0,
	"environment_id": "34580677-e16fa09e-8a8f-47e8-a8d2-9c61ce354e90",
	"totalPass": 1200,
	"delay": "200",
	"persist": true,
	"status": "finished",
	"startedAt": "2024-06-08T16:13:43.377Z",
	"totalFail": 0,
	"results": [
		{
			"id": "273688e3-cfd6-41a0-a69f-2423a730ae18",
			"name": "UserController - login()_admin",
			"url": "http://localhost:8080",
			"time": 8,
			"responseCode": {
				"code": 200,
				"name": "OK"
			},
			"tests": {
				"관리자 로그인 성공": true,
				"응답 시간이 100ms 이내여야 합니다.": true
			},
			"testPassFailCounts": {
				"관리자 로그인 성공": {
					"pass": 50,
					"fail": 0
				},
				"응답 시간이 100ms 이내여야 합니다.": {
					"pass": 1,
					"fail": 0
				}
			},
			"times": [
				33,
				8,
				7,
				6,
				11,
				6,
				8,
				7,
				12,
				11,
				6,
				6,
				8,
				9,
				14,
				9,
				12,
				14,
				9,
				7,
				11,
				12,
				10,
				6,
				10,
				8,
				7,
				25,
				7,
				7,
				10,
				12,
				10,
				12,
				11,
				8,
				9,
				8,
				6,
				10,
				10,
				19,
				9,
				9,
				12,
				11,
				13,
				22,
				9,
				8
			],
			"allTests": [
				{
					"관리자 로그인 성공": true,
					"응답 시간이 100ms 이내여야 합니다.": true
				},
				{
					"관리자 로그인 성공": true,
					"응답 시간이 100ms 이내여야 합니다.": true
				},
				{
					"관리자 로그인 성공": true,
					"응답 시간이 100ms 이내여야 합니다.": true
				},
				{
					"관리자 로그인 성공": true,
					"응답 시간이 100ms 이내여야 합니다.": true
				},
				{
					"관리자 로그인 성공": true,
					"응답 시간이 100ms 이내여야 합니다.": true
				},
				{
					"관리자 로그인 성공": true,
					"응답 시간이 100ms 이내여야 합니다.": true
				},
				{
					"관리자 로그인 성공": true,
					"응답 시간이 100ms 이내여야 합니다.": true
				},
				{
					"관리자 로그인 성공": true,
					"응답 시간이 100ms 이내여야 합니다.": true
				},
				...
				...
```

## Postman collection file

```kotlin
{
	"info": {
		"_postman_id": "7434c7c2-e96f-4d7b-b947-a9d14179039c",
		"name": "AdminFunctionTest",
		"description": "AdminMemberController + AdminSurveyController Get/Post Method HTTP Request Test",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "34580677"
	},
	"item": [
		{
			"name": "UserController - login()_admin",
			"event": [
				{
					"listen": "prerequest",
					"script": {
						"exec": [
							""
						],
						"type": "text/javascript",
						"packages": {}
					}
				},
				{
					"listen": "test",
					"script": {
						"exec": [
							"// 로그인 요청 후 세션 쿠키 저장",
							"pm.test(\"관리자 로그인 성공\", function () {",
							"    pm.response.to.have.status(200);",
							"",
							"    // Set the session cookie for subsequent requests",
							"    var sessionCookie = pm.cookies.get('JSESSIONID'); // 쿠키 이름은 서버 설정에 따라 다를 수 있습니다",
							"    pm.environment.set('sessionCookie', sessionCookie);",
							"});",
							"",
							"pm.test(\"응답 시간이 100ms 이내여야 합니다.\", function () {",
							"    pm.expect(pm.response.responseTime).to.be.below(100);",
							"});"
						],
						"type": "text/javascript",
						"packages": {}
					}
				}
			],
			...
			...
```

# 3.  Related Diagrams

## Sequence Diagram

### Sequence Diagram(Admin)

![SurveyBay-SequenceDiagram(Admin).png](Postman%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%86%AB%20Test%20%E1%84%86%E1%85%B5%E1%86%BE%20Related%20Diagrams%202f909d911dea4bddb81c625b08a89472/SurveyBay-SequenceDiagram(Admin).png)

### Sequence Diagram(Client)

![SurveyBay-SequenceDiagram(Client).png](Postman%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%86%AB%20Test%20%E1%84%86%E1%85%B5%E1%86%BE%20Related%20Diagrams%202f909d911dea4bddb81c625b08a89472/SurveyBay-SequenceDiagram(Client).png)

### Sequence Diagram(Global)

![SurveyBay-SequenceDiagram(Global).png](Postman%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%86%AB%20Test%20%E1%84%86%E1%85%B5%E1%86%BE%20Related%20Diagrams%202f909d911dea4bddb81c625b08a89472/SurveyBay-SequenceDiagram(Global).png)

## Funtion Block Diagram

![image.png](Postman%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%8B%E1%85%B5%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%86%AB%20Test%20%E1%84%86%E1%85%B5%E1%86%BE%20Related%20Diagrams%202f909d911dea4bddb81c625b08a89472/image.png)