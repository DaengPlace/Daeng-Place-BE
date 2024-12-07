# 빌드한 JAR 파일을 위한 경로를 정의
FROM amazoncorretto:17-alpine-jdk AS build

ARG JAR_FILE=build/libs/BackEnd-DaengPlace-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 실행 단계
FROM amazoncorretto:17-alpine-jdk


# 빌드된 JAR 파일을 복사
COPY --from=build app.jar /app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
