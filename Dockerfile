# Dockerfile
# SurveyBay - Dockekfile
#작성자 : 박예림 (21913687), 이홍비 (21912191)
#프로그램 최종 수정 : 2024.6.9. Docker 작성

# 기본 이미지 설정
FROM ubuntu:latest

# 이미지 작성자 정보
LABEL authors="Team_Bay_YU219(Lee & Park)"

# 빌드 단계 - jdk17 ver.
FROM gradle:7.6-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# 전체 프로젝트 복사
COPY . .

# gradlew 실행 권한 추가 => 해당 파일 : 스크립트나 프로그램처럼 실행 가능
RUN chmod +x ./gradlew

# 의존성 다운로드 및 빌드 (테스트 생략)
RUN ./gradlew build -x test --no-daemon

# 실행 단계
FROM openjdk:21-jdk-slim

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /home/gradle/project/build/libs/*.jar /app/application.jar

# 컨테이너 시작 시 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
