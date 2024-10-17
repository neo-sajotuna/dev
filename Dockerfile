FROM ubuntu:latest
LABEL authors="sunsunmacbook"

ENTRYPOINT ["top", "-b"]

# 1단계: 빌드 이미지를 사용하여 JAR 파일을 생성
FROM gradle:7.3.3-jdk17 AS builder
WORKDIR /home/app
COPY . .
RUN gradle clean build --no-daemon

# 2단계: 실제 애플리케이션 실행을 위한 이미지
FROM openjdk:17-jdk-slim
WORKDIR /app
# 빌드된 JAR 파일을 복사
COPY --from=builder /home/app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]