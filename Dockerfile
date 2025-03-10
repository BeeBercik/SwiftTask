FROM maven:3.8.6-eclipse-temurin-17

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/SwiftTask-1.0-SNAPSHOT.jar"]