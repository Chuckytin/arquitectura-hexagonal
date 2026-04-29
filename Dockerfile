FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app
COPY . .
run mvn clean install

CMD mvn spring-boot:run