FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

# Copy Project Files
COPY . .

RUN ./gradlew clean build -x test

VOLUME .gradle/caches

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/backend-0.0.1-SNAPSHOT.jar"]
