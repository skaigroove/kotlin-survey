# Dockerfile
# SurveyBay - Dockekfile
# 작성자 : 박예림 (21913687), 이홍비 (21912191)
# 프로그램 수정
# 2024.6.9. Docker 작성
# 2024.6.10. 한국 표준시 설정 부분 추가 구현

# 기본 이미지 설정
FROM ubuntu:latest

# 이미지 작성자 정보
LABEL authors="Team_Bay_YU219(Lee & Park)"

# 빌드 단계 - jdk17 버전
FROM gradle:7.6-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# 전체 프로젝트 복사
COPY . .

# gradlew 실행 권한 추가
RUN chmod +x ./gradlew

# 의존성 다운로드 및 빌드 (테스트 생략)
RUN ./gradlew build -x test --no-daemon

# 실행 단계
FROM openjdk:21-jdk-slim

# 기준 시간 : 한국 표준시 (KST) => tzdata 설치 + 시간대 설정
# 설정 안 하면 UTC 기준 <=> 영국 표준시 기준
RUN apt-get update && apt-get install -y tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /home/gradle/project/build/libs/*.jar /app/application.jar

# 컨테이너 시작 시 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/application.jar"]

